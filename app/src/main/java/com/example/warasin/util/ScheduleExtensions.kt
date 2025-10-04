package com.example.warasin.util

import com.example.warasin.data.model.DayOfWeek

fun String.toSelectedDaysText(): String {
    if (this.isEmpty()) return "Tidak ada hari dipilih"

    val allDays = DayOfWeek.getAllDays()
    val selectedDayIds = this.split(",")
        .filter { it.isNotEmpty() }
        .mapNotNull { it.toIntOrNull() }

    val dayNames = allDays
        .filter { selectedDayIds.contains(it.id) }
        .map { it.name }

    return when {
        dayNames.isEmpty() -> "Tidak ada hari dipilih"
        dayNames.size == 7 -> "Setiap hari"
        dayNames.size == 1 -> dayNames.first()
        else -> dayNames.joinToString(", ")
    }
}

fun String.toSelectedDaysShortText(): String {
    if (this.isEmpty()) return "-"

    val allDays = DayOfWeek.getAllDays()
    val selectedDayIds = this.split(",")
        .filter { it.isNotEmpty() }
        .mapNotNull { it.toIntOrNull() }

    val dayShortNames = allDays
        .filter { selectedDayIds.contains(it.id) }
        .map { it.shortName }

    return when {
        dayShortNames.isEmpty() -> "-"
        dayShortNames.size == 7 -> "Setiap hari"
        else -> dayShortNames.joinToString(", ")
    }
}