package com.befisc.smslogger

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat

class MessageListenerService: Service() {
    override fun onBind(p0: Intent?): IBinder? {
        return null;
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val smsSender = intent?.getStringExtra(SmsReceiver.MESSAGE_SENDER_KEY) ?: ""
        val smsBody = intent?.getStringExtra(SmsReceiver.MESSAGE_BODY_KEY) ?: ""

        startReadingMessages(smsSender, smsBody)
        return START_STICKY
    }

    private fun startReadingMessages(smsSender: String, smsBody: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "sms_logging"
            val channelName = "SMS Logging"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, channelName, importance)
            val notificationManager =
                applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        // Build the notification
        val notificationBuilder = NotificationCompat.Builder(applicationContext, "sms_logging")
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Set your app's notification icon
            .setContentTitle("sender: $smsSender")
            .setContentText("message: $smsBody")
            .setPriority(NotificationCompat.PRIORITY_MAX)

        // Show the notification
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, notificationBuilder.build())
    }

}