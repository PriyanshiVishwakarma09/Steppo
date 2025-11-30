package com.example.seppo.service

import android.content.Intent
import android.os.IBinder
import android.app.Service

//run even when the app is closed to detects steps
class StepCounterService : Service() {
    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // TODO: Implement pedometer here
        return START_STICKY
    }
}