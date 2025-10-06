package com.example.warasin.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.warasin.data.model.Medicine
import com.example.warasin.data.model.MedicineWithSchedules
import com.example.warasin.data.model.Schedule
import com.example.warasin.data.model.ScheduleWithMedicine
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicineDao {

    // --- Insert Operations ---
    @Insert
    suspend fun insertMedicine(medicine: Medicine): Long

    @Insert
    suspend fun insertSchedules(schedules: Schedule): Long

    // --- Read Operations ---
    @Transaction
    @Query("SELECT * FROM medicines WHERE userId = :userId")
    fun getMedicinesWithSchedulesByUserId(userId: String): Flow<List<MedicineWithSchedules>>

    @Transaction
    @Query("SELECT * FROM medicines WHERE id = :medicineId")
    suspend fun getMedicineById(medicineId: Int): Medicine?

    @Transaction
    @Query("SELECT * FROM medicines WHERE firestoreId = :firestoreId LIMIT 1")
    suspend fun getMedicineByFirestoreId(firestoreId: String): Medicine?

    @Transaction
    @Query("SELECT * FROM medicines WHERE id = :medicineId")
    fun getMedicineWithSchedulesById(medicineId: Int): Flow<MedicineWithSchedules>

    @Transaction
    @Query("SELECT * FROM schedules WHERE userId = :userId ORDER BY time ASC")
    fun getSchedulesWithMedicineByUserId(userId: String): Flow<List<ScheduleWithMedicine>>

    @Transaction
    @Query("SELECT * FROM schedules WHERE medicineId = :medicineId")
    fun getSchedulesByMedicineId(medicineId: Int): Flow<List<Schedule>>

    @Transaction
    @Query("SELECT * FROM schedules WHERE id = :scheduleId")
    fun getScheduleByIdWithMedicine(scheduleId: Int): Flow<ScheduleWithMedicine>

    @Transaction
    @Query("SELECT * FROM schedules WHERE firestoreId = :firestoreId LIMIT 1")
    suspend fun getScheduleByFirestoreId(firestoreId: String): Schedule?

    // --- Sync Queries ---
    @Query("SELECT * FROM medicines WHERE isSynced = 0 AND userId = :userId")
    suspend fun getUnsyncedMedicines(userId: String): List<Medicine>

    @Query("SELECT * FROM schedules WHERE isSynced = 0 AND userId = :userId")
    suspend fun getUnsyncedSchedules(userId: String): List<Schedule>

    @Query("UPDATE medicines SET isSynced = 1, firestoreId = :firestoreId WHERE id = :localId")
    suspend fun markMedicineAsSynced(localId: Int, firestoreId: String)

    @Query("UPDATE schedules SET isSynced = 1, firestoreId = :firestoreId WHERE id = :localId")
    suspend fun markScheduleAsSynced(localId: Int, firestoreId: String)

    // --- Update Operations ---
    @Update
    suspend fun updateMedicine(medicine: Medicine)

    @Update
    suspend fun updateSchedule(schedule: Schedule)

    @Query("UPDATE schedules SET isTaken = :isTaken, isSynced = 0 WHERE id = :scheduleId")
    suspend fun updateScheduleTakenStatus(scheduleId: Int, isTaken: Boolean)

    // --- Delete Operations ---
    @Query("DELETE FROM medicines WHERE id = :medicineId")
    suspend fun deleteMedicineById(medicineId: Int)

    @Query("DELETE FROM schedules WHERE id = :scheduleId")
    suspend fun deleteScheduleById(scheduleId: Int)

    @Query("DELETE FROM medicines WHERE firestoreId = :firestoreId")
    suspend fun deleteMedicineByFirestoreId(firestoreId: String)

    @Query("DELETE FROM schedules WHERE firestoreId = :firestoreId")
    suspend fun deleteScheduleByFirestoreId(firestoreId: String)
}