package com.example.seppo.receiver


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.seppo.service.StepCounterService

//BootReceiver = “Restart my pedometer when phone restarts.”
class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED ||
            intent.action == Intent.ACTION_LOCKED_BOOT_COMPLETED) {

            val serviceIntent = Intent(context, StepCounterService::class.java)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(serviceIntent)   // API 26+
            } else {
                context.startService(serviceIntent)            // API 24–25
            }
        }
    }
}

//Without BootReceiver:
//You restart phone → pedometer stops working until you manually open the app.
//With BootReceiver:
//You restart phone → your app auto-starts counting steps again
//No need to open the app manually.
