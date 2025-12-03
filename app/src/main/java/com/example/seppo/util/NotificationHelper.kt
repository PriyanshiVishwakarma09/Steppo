package com.example.seppo.util

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.seppo.R

object NotificationHelper {
    const val CHANNEL_ID = "pedometer_channel"
    const val CHANNEL_NAME = "Pedometer"
    const val NOTIF_ID = 1001

    fun createChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW)
            channel.setShowBadge(false)
            nm.createNotificationChannel(channel)
        }
    }

    fun buildNotification(context: Context, stepsText: String): Notification {
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("Steppo — Step Tracker")
            .setContentText(stepsText)
            .setSmallIcon(R.drawable.img) // supply your small icon
            .setOngoing(true)
            .setOnlyAlertOnce(true)
        return builder.build()
    }
}