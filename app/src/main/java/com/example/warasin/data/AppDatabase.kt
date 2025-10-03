package com.example.warasin.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.warasin.data.dao.HealthNoteDao
import com.example.warasin.data.dao.MedicineDao
import com.example.warasin.data.model.HealthNote
import com.example.warasin.data.model.Medicine
import com.example.warasin.data.model.Schedule

@Database(
    entities = [Medicine::class, Schedule::class, HealthNote::class],
    version = 4,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun medicineDao(): MedicineDao
    abstract fun healthNoteDao(): HealthNoteDao
}