package com.example.warasin.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "medicines")
data class Medicine(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val dosage: String,
    val times: List<String>,
    val notes: String,
    val isTaken: Boolean = false
)