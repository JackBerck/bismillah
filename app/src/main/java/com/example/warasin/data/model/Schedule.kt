package com.example.warasin.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "schedules",
    foreignKeys = [
        ForeignKey(
            entity = Medicine::class,
            parentColumns = ["id"],
            childColumns = ["medicineId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Schedule(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val medicineId: Int,
    val time: String,
    val selectedDays: String,
    val isTaken: Boolean = false
)
