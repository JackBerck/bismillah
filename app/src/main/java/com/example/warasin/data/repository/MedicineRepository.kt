package com.example.warasin.data.repository

import com.example.warasin.data.dao.MedicineDao
import com.example.warasin.data.model.Medicine
import com.example.warasin.data.model.MedicineWithSchedules
import com.example.warasin.data.model.Schedule
import com.example.warasin.data.model.ScheduleWithMedicine
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MedicineRepository @Inject constructor(
    private val medicineDao: MedicineDao
) {

    // --- READ ---
    fun getAllMedicines(): Flow<List<MedicineWithSchedules>> {
        return medicineDao.getMedicinesWithSchedules()
    }

    fun getMedicineById(id: Int): Flow<MedicineWithSchedules> {
        return medicineDao.getMedicineWithSchedulesById(id)
    }

    fun getAllSchedules(): Flow<List<ScheduleWithMedicine>> {
        return medicineDao.getSchedulesWithMedicine()
    }

    fun getSchedulesForMedicine(medicineId: Int): Flow<List<Schedule>> {
        return medicineDao.getSchedulesByMedicineId(medicineId)
    }

    fun getScheduleByIdWithMedicine(scheduleId: Int): Flow<ScheduleWithMedicine> {
        return medicineDao.getScheduleByIdWithMedicine(scheduleId)
    }

    // --- CREATE ---
    suspend fun addMedicine(medicine: Medicine) {
        medicineDao.insertMedicine(medicine)
    }

    suspend fun addSchedule(schedule: Schedule): Long {
        return medicineDao.insertSchedules(schedule)
    }

    // --- UPDATE ---
    suspend fun updateMedicine(medicine: Medicine) {
        medicineDao.updateMedicine(medicine)
    }

    suspend fun updateSchedule(schedule: Schedule) {
        medicineDao.updateSchedule(schedule)
    }

    suspend fun updateScheduleStatus(scheduleId: Int, isTaken: Boolean) {
        medicineDao.updateScheduleTakenStatus(scheduleId, isTaken)
    }


    // --- DELETE ---
    suspend fun deleteMedicine(medicineId: Int) {
        medicineDao.deleteMedicineById(medicineId)
    }

    suspend fun deleteSchedule(scheduleId: Int) {
        medicineDao.deleteScheduleById(scheduleId)
    }
}
