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
    val userId: String = "", // Tambahkan userId
    val medicineId: Int,
    val time: String,
    val selectedDays: String,
    val isTaken: Boolean = false,
    val timestamp: Long = System.currentTimeMillis(),
    val isSynced: Boolean = false, // Tracking sync status
    val firestoreId: String? = null, // ID dari Firestore
    val medicineFirestoreId: String? = null // Reference ke Firestore medicine
)