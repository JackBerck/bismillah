package com.example.warasin.ui.component

import android.app.TimePickerDialog
import android.content.Context
import androidx.compose.runtime.Composable
import java.util.Calendar

@Composable
fun TimePicker(
    context: Context,
    onTimeSelected: (String) -> Unit
) {
    val calendar = Calendar.getInstance()
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)

    val timePickerDialog = TimePickerDialog(
        context,
        { _, selectedHour: Int, selectedMinute: Int ->
            onTimeSelected("$selectedHour:$selectedMinute")
        }, hour, minute, true // true untuk format 24 jam
    )

    // Tampilkan dialog
    timePickerDialog.show()
}