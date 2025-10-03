package com.example.warasin.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.warasin.data.model.Medicine
import com.example.warasin.data.model.Schedule
import com.example.warasin.data.model.ScheduleWithMedicine
import java.util.Calendar

class AlarmScheduler(private val context: Context) {
    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    fun schedule(schedule: ScheduleWithMedicine) {
        val (hour, minute) = schedule.schedule.time.split(":").map { it.toInt() }

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)

            // Kurangi 5 menit untuk countdown
            add(Calendar.MINUTE, 1)

            // Jika waktu sudah lewat, jadwalkan untuk hari berikutnya
            if (before(Calendar.getInstance())) {
                add(Calendar.DATE, 1)
            }
        }

        // Generate unique notification ID
        val notificationId = (schedule.schedule.id * 1000 + schedule.medicine.id)

        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("MEDICINE_NAME", schedule.medicine.name)
            putExtra("DOSAGE", schedule.medicine.dosage)
            putExtra("NOTIFICATION_ID", notificationId)
            putExtra("MEDICINE_ID", schedule.medicine.id)
            putExtra("ACTUAL_TIME", schedule.schedule.time)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            notificationId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Set exact alarm
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

        // Log untuk debug
        println("Alarm scheduled for: ${calendar.time} with ID: $notificationId")
    }

    fun cancel(schedule: ScheduleWithMedicine) {
        val notificationId = (schedule.schedule.id * 1000 + schedule.medicine.id)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            notificationId,
            Intent(context, AlarmReceiver::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }
}