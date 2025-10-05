package com.example.warasin.data.model

import androidx.room.Embedded
import androidx.room.Relation

data class ScheduleWithMedicine(
    @Embedded
    val schedule: Schedule,

    @Relation(
        parentColumn = "medicineId",
        entityColumn = "id"
    )
    val medicine: Medicine
)
