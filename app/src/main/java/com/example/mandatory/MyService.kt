package com.example.mandatory

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat

class MyService : Service() {

    private lateinit var callback: ServiceCallback
    private val CHANNEL_ID = "MyServiceChannel"

    interface ServiceCallback {
        fun onDataReceived(data: String)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Ambil callback MyApplication
        val application = application as mandatory


        callback = application.callback ?: run {
            stopSelf()
            return START_NOT_STICKY
        }


        if (intent?.action == "OK_PRESSED") {
            callback.onDataReceived("Hi ini data dari service")

            stopForeground(true)
            stopSelf()
        } else {
            showNotification()
        }

        return START_NOT_STICKY
    }

    private fun showNotification() {
        val okIntent = Intent(this, MyService::class.java).apply {
            action = "OK_PRESSED"
        }

        val okPendingIntent = PendingIntent.getService(
            this,
            0,
            okIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )


        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("MyService Notification")
            .setContentText("Tekan OK untuk mengirim data")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .addAction(android.R.drawable.ic_input_add, "OK", okPendingIntent)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)

            .build()


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "My Service Channel",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Channel untuk MyService"
            }

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }



        startForeground(1, notification)
    }


}
