package com.example.warasin.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import androidx.core.app.NotificationCompat
import com.example.warasin.MainActivity
import com.example.warasin.R

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val medicineName = intent.getStringExtra("MEDICINE_NAME") ?: "Waktunya Minum Obat!"
        val dosage = intent.getStringExtra("DOSAGE") ?: ""
        val notificationId = intent.getIntExtra("NOTIFICATION_ID", 0)
        val medicineId = intent.getIntExtra("MEDICINE_ID", 0)
        val actualTime = intent.getStringExtra("ACTUAL_TIME") ?: ""

        startCountdownNotification(context, medicineName, dosage, actualTime, notificationId, medicineId)
    }

    private fun startCountdownNotification(
        context: Context,
        medicineName: String,
        dosage: String,
        actualTime: String,
        notificationId: Int,
        medicineId: Int,
    ) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val handler = Handler(Looper.getMainLooper())

        val appIntent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            notificationId,
            appIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        var remainingSeconds = 300
        var soundPlayed = false

        val countdownRunnable = object : Runnable {
            override fun run() {
                if (remainingSeconds > 0) {
                    val minutes = remainingSeconds / 60
                    val seconds = remainingSeconds % 60
                    val timerText = String.format("%02d:%02d", minutes, seconds)

                    val builder = NotificationCompat.Builder(context, "medicine_channel_id")
                        .setSmallIcon(R.drawable.outline_medication_24)
                        .setContentTitle("⏰ $medicineName dalam $timerText")
                        .setContentText("Siap-siap minum obat: $dosage pada $actualTime")
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setOngoing(true)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(false)
                        .setOnlyAlertOnce(true)
                        .setSilent(true)

                    if (!soundPlayed) {
                        builder.setDefaults(NotificationCompat.DEFAULT_SOUND)
                        soundPlayed = true
                    }

                    notificationManager.notify(notificationId, builder.build())
                    remainingSeconds--
                    handler.postDelayed(this, 1000)
                } else {
                    // Timer habis, ubah notifikasi jadi final
                    val finalNotification = NotificationCompat.Builder(context, "medicine_channel_id")
                        .setSmallIcon(R.drawable.outline_medication_24)
                        .setContentTitle("🔔 WAKTUNYA MINUM OBAT!")
                        .setContentText("$medicineName - $dosage")
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .setOngoing(false)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(false)
                        .setDefaults(NotificationCompat.DEFAULT_ALL)
                        .addAction(
                            R.drawable.outline_medication_24,
                            "Sudah Diminum",
                            createTakenIntent(context, notificationId, medicineId),
                        )
                        .addAction(
                            android.R.drawable.ic_menu_close_clear_cancel,
                            "Sembunyikan",
                            createHideIntent(context, notificationId, medicineId)
                        )
                        .build()

                    notificationManager.notify(notificationId, finalNotification)
                }
            }
        }

        handler.post(countdownRunnable)
    }

    private fun createTakenIntent(context: Context, notificationId: Int, medicineId: Int): PendingIntent {
        val takenIntent = Intent(context, NotificationActionReceiver::class.java).apply {
            putExtra("NOTIFICATION_ID", notificationId)
            putExtra("MEDICINE_ID", medicineId)
            putExtra("ACTION", "TAKEN")
        }
        return PendingIntent.getBroadcast(
            context,
            notificationId + 1000,
            takenIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun createHideIntent(context: Context, notificationId: Int, medicineId: Int): PendingIntent {
        val hideIntent = Intent(context, NotificationActionReceiver::class.java).apply {
            putExtra("NOTIFICATION_ID", notificationId)
            putExtra("MEDICINE_ID", medicineId)
            putExtra("ACTION", "HIDE")
        }
        return PendingIntent.getBroadcast(
            context,
            notificationId + 2000,
            hideIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
}