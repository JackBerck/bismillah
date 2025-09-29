package com.example.warasin.data

import androidx.room.*
import com.example.warasin.data.dao.HealthNoteDao
import com.example.warasin.data.dao.MedicineDao
import com.example.warasin.data.model.HealthNote
import com.example.warasin.data.model.Medicine
import com.example.warasin.util.ListStringConverter

@Database(
    entities = [Medicine::class, HealthNote::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(ListStringConverter::class)
public abstract class AppDatabase : RoomDatabase() {
    abstract fun medicineDao(): MedicineDao
    abstract fun healthNoteDao(): HealthNoteDao
}