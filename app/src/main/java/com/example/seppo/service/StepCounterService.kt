package com.example.seppo.service

import android.app.Service
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.IBinder
import androidx.annotation.WorkerThread
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import com.example.seppo.datastore.DataStoreManager
import com.example.seppo.reposiitory.StepRepository
import com.example.seppo.util.DateUtils
import javax.inject.Inject
import kotlin.math.roundToLong
import com.example.seppo.util.NotificationHelper


@AndroidEntryPoint
class StepCounterService : Service(), SensorEventListener {
    @Inject lateinit var repository: StepRepository
    @Inject lateinit var dataStoreManager: DataStoreManager

    private lateinit var sensorManager: SensorManager
    private var stepSensor: Sensor? = null

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()
        NotificationHelper.createChannel(this)
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        // Start foreground with initial notification text
        startForeground(NotificationHelper.NOTIF_ID, NotificationHelper.buildNotification(this, "Starting..."))

        // Register sensor listener (TYPE_STEP_COUNTER)
        stepSensor?.let { sensor ->
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        } ?: run {
            // No step counter sensor — consider fallback or stop service
            stopSelf()
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // keep it sticky so OS tries to restart
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            sensorManager.unregisterListener(this)
        } catch (t: Throwable) { /* ignore */ }
        scope.cancel()
    }

    /**
     * SensorEventListener callback
     *
     * TYPE_STEP_COUNTER delivers a value that is the number of steps since last boot (float).
     * We'll compute delta between consecutive sensor values and attribute that delta to today's row.
     */
    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null) return
        if (event.sensor.type != Sensor.TYPE_STEP_COUNTER) return

        // float value from sensor: steps since boot
        val sensorValue = event.values[0].toDouble().roundToLong()
        // handle in background coroutine
        scope.launch {
            handleSensorValue(sensorValue)
        }
    }

    @WorkerThread
    private suspend fun handleSensorValue(sensorValue: Long) {
        // Get last stored sensor value & lastSavedDate from DataStore
        val lastSensorTotal = dataStoreManager.getLastSensorTotal()
        val lastSavedDate = dataStoreManager.getLastSavedDate()
        val today = DateUtils.todayKey()

        // 1) If date changed since last saved, (midnight rollover)
        if (lastSavedDate != null && lastSavedDate != today) {
            // nothing special to do because repository stores per-date.
            // just ensure lastSavedDate is updated to today so new increments go to today's row
            dataStoreManager.setLastSavedDate(today)
            // Also we keep lastSensorTotal: it still corresponds to previous boot value.
        } else if (lastSavedDate == null) {
            // first run ever, store today's date
            dataStoreManager.setLastSavedDate(today)
        }
        // 2) If we have no lastSensorTotal -> initialize and return (no increment)
        if (lastSensorTotal == null) {
            dataStoreManager.setLastSensorTotal(sensorValue)
            // update notification showing 0 or current stored value
            val currentToday = repository.getTodayStepsFlow().first()
            updateNotification(currentToday)
            return
        }

        // 3) Compute delta between consecutive values
        val delta: Long = if (sensorValue >= lastSensorTotal) {
            // common case: sensor increased normally
            sensorValue - lastSensorTotal
        } else {
            // sensorValue < lastSensorTotal -> device rebooted or sensor reset
            // In this case treat the sensorValue as steps since boot; we do not add lastSensorTotal
            // as it belonged to previous boot. So add sensorValue as delta.
            sensorValue
        }

        if (delta > 0) {
            // increment Room by delta (cast to Int carefully)
            val inc = if (delta > Int.MAX_VALUE) Int.MAX_VALUE else delta.toInt()
            repository.incrementSteps(inc)
        }

        // 4) persist new lastSensorTotal and lastSavedDate
        dataStoreManager.setLastSensorTotal(sensorValue)
        dataStoreManager.setLastSavedDate(today)

        // 5) update notification to reflect latest today's count
        val current = repository.getTodayStepsFlow().first()
        updateNotification(current)
    }

    private fun updateNotification(stepsToday: Int) {
        val notif = NotificationHelper.buildNotification(this, "Steps today: $stepsToday")
        // update existing foreground notification
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val nm = getSystemService(NOTIFICATION_SERVICE) as android.app.NotificationManager
            nm.notify(NotificationHelper.NOTIF_ID, notif)
        } else {
            // older method
            startForeground(NotificationHelper.NOTIF_ID, notif)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) { /* no-op */ }
}
