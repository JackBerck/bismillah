package com.example.warasin.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "medicines")
data class Medicine(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: String = "", // Tambahkan userId
    val name: String,
    val dosage: String,
    val notes: String?,
    val timestamp: Long = System.currentTimeMillis(),
    val isSynced: Boolean = false, // Tracking sync status
    val firestoreId: String? = null // ID dari Firestore
)