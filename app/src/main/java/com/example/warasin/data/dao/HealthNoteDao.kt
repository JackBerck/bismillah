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

    @Update
    suspend fun updateHealthNote(health_note: HealthNote)

    @Query("SELECT * FROM health_notes WHERE userId = :userId ORDER BY timestamp DESC")
    fun getHealthNotesByUserId(userId: String): Flow<List<HealthNote>>

    @Query("SELECT * FROM health_notes WHERE id = :healthNoteId")
    suspend fun getHealthNoteById(healthNoteId: Int): HealthNote?

    @Query("SELECT * FROM health_notes WHERE firestoreId = :firestoreId LIMIT 1")
    suspend fun getHealthNoteByFirestoreId(firestoreId: String): HealthNote?

    @Query("SELECT * FROM health_notes WHERE isSynced = 0 AND userId = :userId")
    suspend fun getUnsyncedHealthNotes(userId: String): List<HealthNote>

    @Query("UPDATE health_notes SET isSynced = 1, firestoreId = :firestoreId WHERE id = :localId")
    suspend fun markAsSynced(localId: Int, firestoreId: String)

    @Query("DELETE FROM health_notes WHERE firestoreId = :firestoreId")
    suspend fun deleteByFirestoreId(firestoreId: String)
}