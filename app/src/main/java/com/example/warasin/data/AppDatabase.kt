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
    version = 7, // Increment version
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun medicineDao(): MedicineDao
    abstract fun healthNoteDao(): HealthNoteDao

    companion object {
        val MIGRATION_6_7 = object : Migration(6, 7) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Add new columns to medicines table
                database.execSQL("ALTER TABLE medicines ADD COLUMN userId TEXT NOT NULL DEFAULT ''")
                database.execSQL("ALTER TABLE medicines ADD COLUMN timestamp INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE medicines ADD COLUMN isSynced INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE medicines ADD COLUMN firestoreId TEXT")

                // Add new columns to schedules table
                database.execSQL("ALTER TABLE schedules ADD COLUMN userId TEXT NOT NULL DEFAULT ''")
                database.execSQL("ALTER TABLE schedules ADD COLUMN timestamp INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE schedules ADD COLUMN isSynced INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE schedules ADD COLUMN firestoreId TEXT")
                database.execSQL("ALTER TABLE schedules ADD COLUMN medicineFirestoreId TEXT")
            }
        }
    }
}