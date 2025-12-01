package com.example.seppo.service

import android.content.Intent
import android.os.IBinder
import android.app.Service

//run even when the app is closed to detects steps
class StepCounterService : Service() {
    override fun onBind(intent: Intent?): IBinder? = null
//    What is a Bound Service?
//    A bound service lets an Activity talk directly to the service, like:
//    Music apps
//    Download manager
//    Bluetooth service
//    But for a pedometer, we do NOT need that.
//    So we return null → meaning Not a bound service

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // TODO: Implement pedometer here
        return START_STICKY
    }
}

//START_STICKY
//Restart automatically (without intent)
//It is started when you call:
//startService(Intent(context, StepCounterService::class.java))

//service is like a component that continues when app is closed and can run long task and even listen to the step sensor