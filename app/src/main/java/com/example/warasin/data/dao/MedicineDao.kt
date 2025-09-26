package com.example.warasin.data.dao

import androidx.room.*
import com.example.warasin.data.model.Medicine
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicineDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedicine(medicine: Medicine): Long

    @Delete
    suspend fun deleteMedicine(medicine: Medicine)

    @Query("SELECT * FROM medicines ORDER BY id DESC")
    fun getAllMedicines(): Flow<List<Medicine>>

    @Query("UPDATE medicines SET isTaken = :isTaken WHERE id = :medicineId")
    suspend fun updateMedicineTaken(medicineId: Int, isTaken: Boolean)

    @Query("SELECT * FROM medicines WHERE id = :medicineId")
    suspend fun getMedicineById(medicineId: Int): Medicine?
}