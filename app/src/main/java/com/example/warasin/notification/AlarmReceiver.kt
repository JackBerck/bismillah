package com.example.warasin.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
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

        // Langsung tampilkan notifikasi minum obat
        showMedicineNotification(context, medicineName, dosage, actualTime, notificationId, medicineId)
    }

    private fun showMedicineNotification(
        context: Context,
        medicineName: String,
        dosage: String,
        actualTime: String,
        notificationId: Int,
        medicineId: Int,
    ) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val appIntent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            notificationId,
            appIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, "medicine_channel_id")
            .setSmallIcon(R.drawable.outline_medication_24)
            .setContentTitle("🔔 WAKTUNYA MINUM OBAT!")
            .setContentText("$medicineName - $dosage pada $actualTime")
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
            .addAction(
                R.drawable.baseline_snooze_24, // Tambahkan icon snooze jika ada
                "Snooze 10 Menit",
                createSnoozeIntent(context, notificationId, medicineId)
            )
            .build()

        notificationManager.notify(notificationId, notification)
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

    private fun createSnoozeIntent(context: Context, notificationId: Int, medicineId: Int): PendingIntent {
        val snoozeIntent = Intent(context, NotificationActionReceiver::class.java).apply {
            putExtra("NOTIFICATION_ID", notificationId)
            putExtra("MEDICINE_ID", medicineId)
            putExtra("ACTION", "SNOOZE")
        }
        return PendingIntent.getBroadcast(
            context,
            notificationId + 3000,
            snoozeIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
}