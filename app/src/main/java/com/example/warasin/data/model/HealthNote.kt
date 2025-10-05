package com.example.warasin.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "health_notes")
data class HealthNote(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: String = "",
    val bloodPressure: String,
    val bloodSugar: String,
    val bodyTemperature: String,
    val mood: String,
    val notes: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isSynced: Boolean = false, // Tracking sync status
    val firestoreId: String? = null // ID dari Firestore
)