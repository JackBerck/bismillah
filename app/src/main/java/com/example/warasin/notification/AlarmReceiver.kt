package com.example.warasin.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.warasin.MainActivity
import com.example.warasin.R
import com.example.warasin.data.repository.MedicineRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var repository: MedicineRepository

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            val pendingResult = goAsync()
            val alarmScheduler = AlarmScheduler(context)

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val allSchedules = repository.getAllSchedules().first()
                    allSchedules.forEach { schedule ->
                        alarmScheduler.schedule(schedule)
                    }
                    println("Rescheduling ${allSchedules.size} alarms after boot.")
                } finally {
                    pendingResult.finish()
                }
            }
            return
        }

        val medicineName = intent.getStringExtra("MEDICINE_NAME") ?: "Waktunya Minum Obat!"
        val dosage = intent.getStringExtra("DOSAGE") ?: ""
        val notificationId = intent.getIntExtra("NOTIFICATION_ID", 0)
        val medicineId = intent.getIntExtra("MEDICINE_ID", 0)
        val actualTime = intent.getStringExtra("ACTUAL_TIME") ?: ""
        val scheduleId = intent.getIntExtra("SCHEDULE_ID", 0)

        showMedicineNotification(context, medicineName, dosage, actualTime, notificationId, medicineId, scheduleId)
    }

    private fun showMedicineNotification(
        context: Context,
        medicineName: String,
        dosage: String,
        actualTime: String,
        notificationId: Int,
        medicineId: Int,
        scheduleId: Int
    ) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "medicine_reminder_channel"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = "Pengingat Minum Obat"
            val channelDescription = "Notifikasi untuk jadwal minum obat."
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, channelName, importance).apply {
                description = channelDescription
                val soundUri = Uri.parse("android.resource://${context.packageName}/${R.raw.clock_alarm}")
                setSound(soundUri, null)
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 500, 200, 500)
            }
            notificationManager.createNotificationChannel(channel)
        }

        val appIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            notificationId,
            appIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val soundUri = Uri.parse("android.resource://${context.packageName}/${R.raw.clock_alarm}")

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.outline_medication_24)
            .setContentTitle("🔔 WAKTUNYA MINUM OBAT!")
            .setContentText("$medicineName - $dosage pada $actualTime")
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(0, 500, 200, 500))
            .setSound(soundUri)
            .addAction(
                R.drawable.outline_medication_24,
                "Sudah Diminum",
                createTakenIntent(context, notificationId, medicineId, scheduleId),
            )
            .addAction(
                android.R.drawable.ic_menu_close_clear_cancel,
                "Sembunyikan",
                createHideIntent(context, notificationId, medicineId)
            )
            .addAction(
                R.drawable.baseline_snooze_24,
                "Snooze 10 Menit",
                createSnoozeIntent(context, notificationId, medicineId)
            )
            .build()

        notificationManager.notify(notificationId, notification)
    }

    private fun createTakenIntent(context: Context, notificationId: Int, medicineId: Int, scheduleId: Int): PendingIntent {
        val takenIntent = Intent(context, NotificationActionReceiver::class.java).apply {
            putExtra("NOTIFICATION_ID", notificationId)
            putExtra("MEDICINE_ID", medicineId)
            putExtra("SCHEDULE_ID", scheduleId)
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