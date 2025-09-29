package com.example.warasin.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun formatTimestamp(timestamp: Long): String {
    val sdf = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("id", "ID"))
    return sdf.format(Date(timestamp))
}