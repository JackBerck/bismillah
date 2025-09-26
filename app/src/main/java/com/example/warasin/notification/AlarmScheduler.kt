package com.example.warasin.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.warasin.data.model.Medicine
import java.util.Calendar

class AlarmScheduler(private val context: Context) {
    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    fun schedule(medicine: Medicine) {
        medicine.times.forEachIndexed { timeIndex, time ->
            val (hour, minute) = time.split(":").map { it.toInt() }

            val calendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)

                add(Calendar.MINUTE, -5)

                if (before(Calendar.getInstance())) {
                    add(Calendar.DATE, 1)
                }
            }

            val notificationId = "${medicine.id}${timeIndex}".toInt()

            val intent = Intent(context, AlarmReceiver::class.java).apply {
                putExtra("MEDICINE_NAME", medicine.name)
                putExtra("DOSAGE", medicine.dosage)
                putExtra("NOTIFICATION_ID", notificationId)
                putExtra("MEDICINE_ID", medicine.id) // Tambahkan medicine ID
                putExtra("ACTUAL_TIME", time)
            }

            val pendingIntent = PendingIntent.getBroadcast(
                context,
                notificationId,
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

    fun cancel(medicine: Medicine) {
        medicine.times.forEachIndexed { timeIndex, _ ->
            val notificationId = "${medicine.id}${timeIndex}".toInt()
            alarmManager.cancel(
                PendingIntent.getBroadcast(
                    context,
                    notificationId,
                    Intent(context, AlarmReceiver::class.java),
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            )
        }
    }
}