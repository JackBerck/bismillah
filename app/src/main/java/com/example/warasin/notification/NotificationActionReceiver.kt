package com.example.warasin.notification

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.warasin.data.AppDatabase
import com.example.warasin.data.model.Medicine
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class NotificationActionReceiver : BroadcastReceiver() {

    @Inject
    lateinit var database: AppDatabase

    override fun onReceive(context: Context, intent: Intent) {
        val notificationId = intent.getIntExtra("NOTIFICATION_ID", 0)
        val action = intent.getStringExtra("ACTION")
        val medicineId = intent.getIntExtra("MEDICINE_ID", 0)
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        when (action) {
            "TAKEN" -> {
                // Update isTaken menjadi true
                CoroutineScope(Dispatchers.IO).launch {
                    database.medicineDao().updateScheduleTakenStatus(medicineId, true)
                }
                notificationManager.cancel(notificationId)
            }
            "HIDE" -> {
                // Sembunyikan notifikasi saja
                notificationManager.cancel(notificationId)
            }
            "SNOOZE" -> {
                notificationManager.cancel(notificationId)
                scheduleSnoozeNotification(context, notificationId, medicineId)
            }
        }
    }

    private fun scheduleSnoozeNotification(context: Context, notificationId: Int, medicineId: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val calendar = Calendar.getInstance().apply {
            add(Calendar.MINUTE, 5)
        }

        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("MEDICINE_NAME", "Pengingat Snooze")
            putExtra("DOSAGE", "Jangan lupa minum obat")
            putExtra("NOTIFICATION_ID", notificationId)
            putExtra("MEDICINE_ID", medicineId)
            putExtra("ACTUAL_TIME", "Sekarang")
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            notificationId + 3000,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        } else {
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        }
    }
}