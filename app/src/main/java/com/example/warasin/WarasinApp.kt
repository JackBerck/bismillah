package com.example.warasin

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentResolver
import android.content.Context
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import dagger.hilt.android.HiltAndroidApp
import kotlin.text.compareTo

@HiltAndroidApp
class WarasinApp : Application() {
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(NotificationManager::class.java)

            try {
                notificationManager.deleteNotificationChannel("medicine_channel_id")
            } catch (e: Exception) {
            }

            val soundUri = Uri.parse("android.resource://$packageName/${R.raw.clock_alarm}")
            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_ALARM) // Ubah ke USAGE_ALARM agar lebih keras
                .build()

            val channel = NotificationChannel(
                "medicine_channel_id",
                "Medicine Reminders",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Channel for medicine reminder notifications"
                setSound(soundUri, audioAttributes)
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 500, 200, 500)
            }

            notificationManager.createNotificationChannel(channel)
        }
    }
}