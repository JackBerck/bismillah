package com.example.warasin.util

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun stringToCalendar(timeString: String): Calendar? {
    return try {
        val format = SimpleDateFormat("HH:mm", Locale.getDefault())
        val date = format.parse(timeString)
        Calendar.getInstance().apply {
            time = date
        }
    } catch (e: ParseException) {
        e.printStackTrace()
        null
    }
}
