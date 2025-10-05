package com.example.warasin.ui.medicine

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.warasin.data.model.Medicine
import com.example.warasin.data.model.MedicineWithSchedules
import com.example.warasin.data.repository.MedicineRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MedicineViewModel @Inject constructor(
    private val repository: MedicineRepository
) : ViewModel() {
    private val _medicines = MutableStateFlow<List<MedicineWithSchedules>>(emptyList())
    val medicines: StateFlow<List<MedicineWithSchedules>> = _medicines.asStateFlow()

    private val _selectedMedicine = MutableStateFlow<MedicineWithSchedules?>(null)
    val selectedMedicine: StateFlow<MedicineWithSchedules?> = _selectedMedicine.asStateFlow()


    init {
        loadAllMedicines()
    }

    private fun loadAllMedicines() {
        viewModelScope.launch {
            repository.getAllMedicines()
                .catch { exception ->
                    println("Error loading medicines: ${exception.message}")
                }
                .collect { medicineList ->
                    _medicines.value = medicineList
                }
        }
    }

    fun loadMedicineById(id: Int) {
        viewModelScope.launch {
            repository.getMedicineById(id)
                .catch { e -> _selectedMedicine.value = null }
                .collect { _selectedMedicine.value = it }
        }
    }

    fun addMedicine(name: String, dosage: String, notes: String) {
        viewModelScope.launch {
            val newMedicine = Medicine(name = name, dosage = dosage, notes = notes)
            repository.addMedicine(newMedicine)
        }
    }

    fun updateMedicineDetails(medicine: Medicine) {
        viewModelScope.launch {
            repository.updateMedicine(medicine)
        }
    }

    fun toggleScheduleTakenStatus(scheduleId: Int, isCurrentlyTaken: Boolean) {
        viewModelScope.launch {
            repository.updateScheduleStatus(scheduleId, !isCurrentlyTaken)
        }
    }

    fun deleteMedicine(medicineId: Int) {
        viewModelScope.launch {
            repository.deleteMedicine(medicineId)
        }
    }
}
