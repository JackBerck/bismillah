package com.example.warasin.ui.medicine

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.warasin.data.dao.MedicineDao
import com.example.warasin.data.model.Medicine
import com.example.warasin.notification.AlarmScheduler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class MedicineViewModel @Inject constructor(
    private val medicineDao: MedicineDao,
    private val alarmScheduler: AlarmScheduler
) : ViewModel() {
    val currentTime = LocalTime.now()
    val addedTime = currentTime.plusMinutes(6)
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    val formattedTime = addedTime.format(formatter)

    val initialMedicines = listOf(
        Medicine(name = "Paracetamol", dosage = "500mg", times = listOf("$formattedTime", "20:00"), notes = "After meals"),
        Medicine(name = "Ibuprofen", dosage = "200mg", times = listOf("12:00"), notes = "With water")
    )

        val medicines = medicineDao.getAllMedicines()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000L),
                initialValue = initialMedicines
            )

    fun addMedicine(name: String, dosage: String, times: List<String>, notes: String) {
        viewModelScope.launch {
            val newMedicine = Medicine(
                name = name,
                dosage = dosage,
                times = times,
                notes = notes
            )
            val medicineId = medicineDao.insertMedicine(newMedicine)

            val medicineWithId = newMedicine.copy(id = medicineId.toInt())
            alarmScheduler.schedule(medicineWithId)
        }
    }

    fun deleteMedicine(medicine: Medicine) {
        viewModelScope.launch {
            medicineDao.deleteMedicine(medicine)
            alarmScheduler.cancel(medicine)
        }
    }
}