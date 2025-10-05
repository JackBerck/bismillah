package com.example.warasin.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.warasin.data.dao.HealthNoteDao
import com.example.warasin.data.dao.MedicineDao
import com.example.warasin.data.model.HealthNote
import com.example.warasin.data.model.Medicine
import com.example.warasin.data.model.Schedule

@Database(
    entities = [Medicine::class, Schedule::class, HealthNote::class],
    version = 6,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun medicineDao(): MedicineDao
    abstract fun healthNoteDao(): HealthNoteDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE health_notes ADD COLUMN userId TEXT NOT NULL DEFAULT ''")
                database.execSQL("ALTER TABLE health_notes ADD COLUMN isSynced INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE health_notes ADD COLUMN firestoreId TEXT")
            }
        }
    }
}