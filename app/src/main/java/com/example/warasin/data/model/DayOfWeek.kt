package com.example.warasin.data.model

data class DayOfWeek(
    val id: Int,
    val name: String,
    val shortName: String
) {
    companion object {
        fun getAllDays() = listOf(
            DayOfWeek(1, "Senin", "Sen"),
            DayOfWeek(2, "Selasa", "Sel"),
            DayOfWeek(3, "Rabu", "Rab"),
            DayOfWeek(4, "Kamis", "Kam"),
            DayOfWeek(5, "Jumat", "Jum"),
            DayOfWeek(6, "Sabtu", "Sab"),
            DayOfWeek(7, "Minggu", "Min")
        )
    }
}