package com.example.warasin.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import com.example.warasin.data.model.ScheduleWithMedicine
import java.util.Calendar

class AlarmScheduler(private val context: Context) {
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun schedule(schedule: ScheduleWithMedicine) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!alarmManager.canScheduleExactAlarms()) {
                Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).also { intent ->
                    Toast.makeText(context, "Izin untuk alarm tepat waktu dibutuhkan", Toast.LENGTH_LONG).show()
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(intent)
                }
                return
            }
        }

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

            set(Calendar.DAY_OF_WEEK, dayOfWeek.toCalendarDay())

            if (before(Calendar.getInstance())) {
                add(Calendar.WEEK_OF_YEAR, 1)
            }
        }

        val notificationId = (schedule.schedule.id * 10000 + schedule.medicine.id * 10 + dayOfWeek)

        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("MEDICINE_NAME", schedule.medicine.name)
            putExtra("DOSAGE", schedule.medicine.dosage)
            putExtra("NOTIFICATION_ID", notificationId)
            putExtra("MEDICINE_ID", schedule.medicine.id)
            putExtra("ACTUAL_TIME", schedule.schedule.time)
            putExtra("DAY_OF_WEEK", dayOfWeek)
            putExtra("SCHEDULE_ID", schedule.schedule.id)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            notificationId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
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
            println("Alarm canceled for ID: $notificationId")
        }
    }

    private fun Int.toCalendarDay(): Int {
        return when (this) {
            1 -> Calendar.MONDAY
            2 -> Calendar.TUESDAY
            3 -> Calendar.WEDNESDAY
            4 -> Calendar.THURSDAY
            5 -> Calendar.FRIDAY
            6 -> Calendar.SATURDAY
            7 -> Calendar.SUNDAY
            else -> Calendar.MONDAY
        }
    }
}