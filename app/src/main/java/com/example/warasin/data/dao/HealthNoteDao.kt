package com.example.warasin.data.dao

import androidx.room.*
import com.example.warasin.data.model.HealthNote
import kotlinx.coroutines.flow.Flow

@Dao
interface HealthNoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHealthNote(health_note: HealthNote): Long

    @Delete
    suspend fun deleteHealthNote(health_note: HealthNote)

    @Query("SELECT * FROM health_notes ORDER BY id DESC")
    fun getAllHealthNote(): Flow<List<HealthNote>>

    @Query("SELECT * FROM health_notes WHERE id = :healthNoteId")
    suspend fun getHealthNoteById(healthNoteId: Int): HealthNote?
}