package com.example.warasin.data.repository

import android.util.Log
import com.example.warasin.data.dao.MedicineDao
import com.example.warasin.data.model.Medicine
import com.example.warasin.data.model.MedicineWithSchedules
import com.example.warasin.data.model.Schedule
import com.example.warasin.data.model.ScheduleWithMedicine
import com.example.warasin.data.model.FirestoreMedicine
import com.example.warasin.data.model.FirestoreSchedule
import com.example.warasin.data.preferences.UserPreferences
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MedicineRepository @Inject constructor(
    private val medicineDao: MedicineDao,
    private val userPreferences: UserPreferences,
    private val firestore: FirebaseFirestore
) {

    private val userId get() = userPreferences.getUserId()

    // --- READ ---
    fun getAllMedicines(): Flow<List<MedicineWithSchedules>> {
        return medicineDao.getMedicinesWithSchedulesByUserId(userId)
    }

    fun getMedicineById(id: Int): Flow<MedicineWithSchedules> {
        return medicineDao.getMedicineWithSchedulesById(id)
    }

    fun getAllSchedules(): Flow<List<ScheduleWithMedicine>> {
        return medicineDao.getSchedulesWithMedicineByUserId(userId)
    }

    fun getSchedulesForMedicine(medicineId: Int): Flow<List<Schedule>> {
        return medicineDao.getSchedulesByMedicineId(medicineId)
    }

    fun getScheduleByIdWithMedicine(scheduleId: Int): Flow<ScheduleWithMedicine> {
        return medicineDao.getScheduleByIdWithMedicine(scheduleId)
    }

    // --- CREATE ---
    suspend fun addMedicine(name: String, dosage: String, notes: String?) {
        val medicine = Medicine(
            userId = userId,
            name = name,
            dosage = dosage,
            notes = notes,
            isSynced = false
        )

        // Save to local database first
        val localId = medicineDao.insertMedicine(medicine)

        // Try to sync to Firestore
        try {
            syncMedicineToFirestore(medicine.copy(id = localId.toInt()))
        } catch (e: Exception) {
            Log.d("MedicineRepository", "Failed to sync medicine immediately: ${e.message}")
        }
    }

    suspend fun addMedicine(medicine: Medicine) {
        val localId = medicineDao.insertMedicine(medicine.copy(userId = userId, isSynced = false))

        try {
            syncMedicineToFirestore(medicine.copy(id = localId.toInt(), userId = userId))
        } catch (e: Exception) {
            Log.d("MedicineRepository", "Failed to sync medicine immediately: ${e.message}")
        }
    }

    suspend fun addSchedule(medicineId: Int, time: String, selectedDays: List<Int>): Long {
        val schedule = Schedule(
            userId = userId,
            medicineId = medicineId,
            time = time,
            selectedDays = selectedDays.joinToString(","),
            isSynced = false
        )

        // Save to local database first
        val localId = medicineDao.insertSchedules(schedule)

        // Try to sync to Firestore
        try {
            syncScheduleToFirestore(schedule.copy(id = localId.toInt()))
        } catch (e: Exception) {
            Log.d("MedicineRepository", "Failed to sync schedule immediately: ${e.message}")
        }

        return localId
    }

    suspend fun addSchedule(schedule: Schedule): Long {
        val localId = medicineDao.insertSchedules(schedule.copy(userId = userId, isSynced = false))

        try {
            syncScheduleToFirestore(schedule.copy(id = localId.toInt(), userId = userId))
        } catch (e: Exception) {
            Log.d("MedicineRepository", "Failed to sync schedule immediately: ${e.message}")
        }

        return localId
    }

    // --- UPDATE ---
    suspend fun updateMedicine(medicine: Medicine) {
        // Update local database
        medicineDao.updateMedicine(medicine.copy(isSynced = false))

        // Try to sync to Firestore
        try {
            syncMedicineToFirestore(medicine)
        } catch (e: Exception) {
            Log.d("MedicineRepository", "Failed to sync medicine update: ${e.message}")
        }
    }

    suspend fun updateSchedule(schedule: Schedule) {
        // Update local database
        medicineDao.updateSchedule(schedule.copy(isSynced = false))

        // Try to sync to Firestore
        try {
            syncScheduleToFirestore(schedule)
        } catch (e: Exception) {
            Log.d("MedicineRepository", "Failed to sync schedule update: ${e.message}")
        }
    }

    suspend fun updateScheduleStatus(scheduleId: Int, isTaken: Boolean) {
        medicineDao.updateScheduleTakenStatus(scheduleId, isTaken)
    }

    // --- DELETE ---
    suspend fun deleteMedicine(medicineId: Int) {
        val medicine = medicineDao.getMedicineById(medicineId)

        // Delete from local database
        medicineDao.deleteMedicineById(medicineId)

        // Delete from Firestore if synced
        try {
            medicine?.firestoreId?.let { firestoreId ->
                firestore.collection("medicines")
                    .document(firestoreId)
                    .delete()
                    .await()
            }
        } catch (e: Exception) {
            Log.d("MedicineRepository", "Failed to delete medicine from Firestore: ${e.message}")
        }
    }

    suspend fun deleteSchedule(scheduleId: Int) {
        // Get schedule first before deleting
        val scheduleWithMedicine = try {
            medicineDao.getScheduleByIdWithMedicine(scheduleId).first()
        } catch (e: Exception) {
            null
        }

        // Delete from local database
        medicineDao.deleteScheduleById(scheduleId)

        // Delete from Firestore if synced
        try {
            scheduleWithMedicine?.schedule?.firestoreId?.let { firestoreId ->
                firestore.collection("schedules")
                    .document(firestoreId)
                    .delete()
                    .await()
            }
        } catch (e: Exception) {
            Log.d("MedicineRepository", "Failed to delete schedule from Firestore: ${e.message}")
        }
    }

    // --- SYNC FUNCTIONS ---
    private suspend fun syncMedicineToFirestore(medicine: Medicine) {
        try {
            val firestoreMedicine = FirestoreMedicine(
                id = medicine.firestoreId ?: "",
                userId = medicine.userId,
                name = medicine.name,
                dosage = medicine.dosage,
                notes = medicine.notes ?: "",
                timestamp = Timestamp(medicine.timestamp / 1000, 0),
                isSynced = true
            )

            val documentRef = if (medicine.firestoreId != null) {
                firestore.collection("medicines").document(medicine.firestoreId)
            } else {
                firestore.collection("medicines").document()
            }

            documentRef.set(firestoreMedicine).await()

            // Mark as synced in local database
            medicineDao.markMedicineAsSynced(medicine.id, documentRef.id)

            Log.d("MedicineRepository", "Medicine synced successfully")
        } catch (e: Exception) {
            Log.e("MedicineRepository", "Failed to sync medicine: ${e.message}")
            throw e
        }
    }

    private suspend fun syncScheduleToFirestore(schedule: Schedule) {
        try {
            // Get medicine's Firestore ID
            val medicine = medicineDao.getMedicineById(schedule.medicineId)
            val medicineFirestoreId = medicine?.firestoreId

            if (medicineFirestoreId == null) {
                Log.w("MedicineRepository", "Cannot sync schedule: medicine not synced yet")
                return
            }

            val firestoreSchedule = FirestoreSchedule(
                id = schedule.firestoreId ?: "",
                userId = schedule.userId,
                medicineId = medicineFirestoreId,
                time = schedule.time,
                selectedDays = schedule.selectedDays,
                isTaken = schedule.isTaken,
                timestamp = Timestamp(schedule.timestamp / 1000, 0),
                isSynced = true
            )

            val documentRef = if (schedule.firestoreId != null) {
                firestore.collection("schedules").document(schedule.firestoreId)
            } else {
                firestore.collection("schedules").document()
            }

            documentRef.set(firestoreSchedule).await()

            // Mark as synced in local database
            medicineDao.markScheduleAsSynced(schedule.id, documentRef.id)

            Log.d("MedicineRepository", "Schedule synced successfully")
        } catch (e: Exception) {
            Log.e("MedicineRepository", "Failed to sync schedule: ${e.message}")
            throw e
        }
    }

    suspend fun syncMedicinesFromFirestore() {
        try {
            Log.d("MedicineRepository", "Fetching medicines from Firestore for user: $userId")

            val firestoreMedicines = firestore.collection("medicines")
                .whereEqualTo("userId", userId)
                .get()
                .await()

            Log.d("MedicineRepository", "Found ${firestoreMedicines.documents.size} medicines in Firestore")

            firestoreMedicines.documents.forEach { document ->
                val firestoreMedicine = document.toObject(FirestoreMedicine::class.java)
                firestoreMedicine?.let { medicine ->
                    // Check if medicine already exists locally
                    val existingMedicine = medicineDao.getMedicineByFirestoreId(document.id)

                    if (existingMedicine == null) {
                        // Add new medicine from Firestore
                        val localMedicine = Medicine(
                            userId = medicine.userId,
                            name = medicine.name,
                            dosage = medicine.dosage,
                            notes = medicine.notes,
                            timestamp = medicine.timestamp.toDate().time,
                            isSynced = true,
                            firestoreId = document.id
                        )
                        medicineDao.insertMedicine(localMedicine)
                        Log.d("MedicineRepository", "Added medicine from Firestore: ${medicine.name}")
                    }
                }
            }

            Log.d("MedicineRepository", "Medicines sync from Firestore completed")
        } catch (e: Exception) {
            Log.e("MedicineRepository", "Failed to sync medicines from Firestore: ${e.message}")
        }
    }

    suspend fun syncSchedulesFromFirestore() {
        try {
            Log.d("MedicineRepository", "Fetching schedules from Firestore for user: $userId")

            val firestoreSchedules = firestore.collection("schedules")
                .whereEqualTo("userId", userId)
                .get()
                .await()

            Log.d("MedicineRepository", "Found ${firestoreSchedules.documents.size} schedules in Firestore")

            firestoreSchedules.documents.forEach { document ->
                val firestoreSchedule = document.toObject(FirestoreSchedule::class.java)
                firestoreSchedule?.let { schedule ->
                    // Check if schedule already exists locally
                    val existingSchedule = medicineDao.getScheduleByFirestoreId(document.id)

                    if (existingSchedule == null) {
                        // Find local medicine by Firestore ID
                        val localMedicine = medicineDao.getMedicineByFirestoreId(schedule.medicineId)
                        localMedicine?.let { medicine ->
                            val localSchedule = Schedule(
                                userId = schedule.userId,
                                medicineId = medicine.id,
                                time = schedule.time,
                                selectedDays = schedule.selectedDays,
                                isTaken = schedule.isTaken,
                                timestamp = schedule.timestamp.toDate().time,
                                isSynced = true,
                                firestoreId = document.id,
                                medicineFirestoreId = schedule.medicineId
                            )
                            medicineDao.insertSchedules(localSchedule)
                            Log.d("MedicineRepository", "Added schedule from Firestore: ${schedule.time}")
                        }
                    }
                }
            }

            Log.d("MedicineRepository", "Schedules sync from Firestore completed")
        } catch (e: Exception) {
            Log.e("MedicineRepository", "Failed to sync schedules from Firestore: ${e.message}")
        }
    }

    suspend fun syncAllUnsyncedData() {
        try {
            // Sync medicines first
            val unsyncedMedicines = medicineDao.getUnsyncedMedicines(userId)
            unsyncedMedicines.forEach { medicine ->
                try {
                    syncMedicineToFirestore(medicine)
                } catch (e: Exception) {
                    Log.e("MedicineRepository", "Failed to sync medicine ${medicine.name}: ${e.message}")
                }
            }

            // Then sync schedules
            val unsyncedSchedules = medicineDao.getUnsyncedSchedules(userId)
            unsyncedSchedules.forEach { schedule ->
                try {
                    syncScheduleToFirestore(schedule)
                } catch (e: Exception) {
                    Log.e("MedicineRepository", "Failed to sync schedule ${schedule.time}: ${e.message}")
                }
            }
        } catch (e: Exception) {
            Log.e("MedicineRepository", "Failed to sync unsynced data: ${e.message}")
        }
    }

    suspend fun syncAllFromFirestore() {
        syncMedicinesFromFirestore()
        syncSchedulesFromFirestore()
    }
}