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
    suspend fun insertSchedules(schedules: List<Schedule>)


    // --- Read Operations ---
    @Transaction
    @Query("SELECT * FROM medicines")
    fun getMedicinesWithSchedules(): Flow<List<MedicineWithSchedules>>

    @Transaction
    @Query("SELECT * FROM medicines WHERE id = :medicineId")
    fun getMedicineWithSchedulesById(medicineId: Int): Flow<MedicineWithSchedules>

    @Transaction
    @Query("SELECT * FROM schedules ORDER BY time ASC")
    fun getSchedulesWithMedicine(): Flow<List<ScheduleWithMedicine>>


    @Transaction
    @Query("SELECT * FROM schedules WHERE medicineId = :medicineId")
    fun getSchedulesByMedicineId(medicineId: Int): Flow<List<Schedule>>


    // --- Update Operations ---
    @Update
    suspend fun updateMedicine(medicine: Medicine)

    @Update
    suspend fun updateSchedule(schedule: Schedule)

    // Query spesifik untuk mengubah status isTaken
    @Query("UPDATE schedules SET isTaken = :isTaken WHERE id = :scheduleId")
    suspend fun updateScheduleTakenStatus(scheduleId: Int, isTaken: Boolean)


    // --- Delete Operations ---
    // Menghapus obat beserta semua jadwalnya (karena onDelete = CASCADE)
    @Query("DELETE FROM medicines WHERE id = :medicineId")
    suspend fun deleteMedicineById(medicineId: Int)

    // Menghapus satu jadwal spesifik
    @Query("DELETE FROM schedules WHERE id = :scheduleId")
    suspend fun deleteScheduleById(scheduleId: Int)
}
