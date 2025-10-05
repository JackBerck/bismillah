package com.example.warasin.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.warasin.data.model.ScheduleWithMedicine
import java.util.Calendar

class AlarmScheduler(private val context: Context) {
    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    fun schedule(schedule: ScheduleWithMedicine) {
        val (hour, minute) = schedule.schedule.time.split(":").map { it.toInt() }
        val selectedDays = schedule.schedule.selectedDays.split(",")
            .filter { it.isNotEmpty() }
            .map { it.toInt() }

        selectedDays.forEach { dayOfWeek ->
            scheduleForSpecificDay(schedule, hour, minute, dayOfWeek)
        }
    }

    private fun scheduleForSpecificDay(
        schedule: ScheduleWithMedicine,
        hour: Int,
        minute: Int,
        dayOfWeek: Int
    ) {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)

            // Set hari dalam seminggu (Calendar.SUNDAY = 1, Calendar.MONDAY = 2, dst)
            val calendarDayOfWeek = when (dayOfWeek) {
                1 -> Calendar.MONDAY
                2 -> Calendar.TUESDAY
                3 -> Calendar.WEDNESDAY
                4 -> Calendar.THURSDAY
                5 -> Calendar.FRIDAY
                6 -> Calendar.SATURDAY
                7 -> Calendar.SUNDAY
                else -> Calendar.MONDAY
            }

            set(Calendar.DAY_OF_WEEK, calendarDayOfWeek)

            // Jika waktu sudah lewat minggu ini, jadwalkan untuk minggu depan
            if (before(Calendar.getInstance())) {
                add(Calendar.WEEK_OF_YEAR, 1)
            }
        }

        // Generate unique notification ID untuk setiap kombinasi schedule + day
        val notificationId = (schedule.schedule.id * 10000 + schedule.medicine.id * 10 + dayOfWeek)

        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("MEDICINE_NAME", schedule.medicine.name)
            putExtra("DOSAGE", schedule.medicine.dosage)
            putExtra("NOTIFICATION_ID", notificationId)
            putExtra("MEDICINE_ID", schedule.medicine.id)
            putExtra("ACTUAL_TIME", schedule.schedule.time)
            putExtra("DAY_OF_WEEK", dayOfWeek)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            notificationId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Set repeating alarm setiap minggu
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY * 7, // Repeat setiap 7 hari
            pendingIntent
        )

        println("Alarm scheduled for: ${calendar.time} (Day: $dayOfWeek) with ID: $notificationId")
    }

    fun cancel(schedule: ScheduleWithMedicine) {
        val selectedDays = schedule.schedule.selectedDays.split(",")
            .filter { it.isNotEmpty() }
            .map { it.toInt() }

        selectedDays.forEach { dayOfWeek ->
            val notificationId = (schedule.schedule.id * 10000 + schedule.medicine.id * 10 + dayOfWeek)
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                notificationId,
                Intent(context, AlarmReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            alarmManager.cancel(pendingIntent)
        }
    }
}