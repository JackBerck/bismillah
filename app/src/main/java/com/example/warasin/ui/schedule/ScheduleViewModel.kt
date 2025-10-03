package com.example.warasin.ui.schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.warasin.data.model.Medicine
import com.example.warasin.data.model.MedicineWithSchedules
import com.example.warasin.data.model.Schedule
import com.example.warasin.data.model.ScheduleWithMedicine
import com.example.warasin.data.repository.MedicineRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val repository: MedicineRepository
) : ViewModel() {
    private val _schedules = MutableStateFlow<List<ScheduleWithMedicine>>(emptyList())
    val schedules: StateFlow<List<ScheduleWithMedicine>> = _schedules.asStateFlow()

    private val _medicines = MutableStateFlow<List<MedicineWithSchedules>>(emptyList())
    val medicines: StateFlow<List<MedicineWithSchedules>> = _medicines.asStateFlow()

    private val _selectedSchedule = MutableStateFlow<ScheduleWithMedicine?>(null)
    val selectedSchedule: StateFlow<ScheduleWithMedicine?> = _selectedSchedule.asStateFlow()


    init {
        loadAllSchedule()
        loadAllMedicines()
    }

    private fun loadAllSchedule() {
        viewModelScope.launch {
            repository.getAllSchedules()
                .catch { exception ->
                    println("Error loading medicines: ${exception.message}")
                }
                .collect { scheduleList ->
                    _schedules.value = scheduleList
                }
        }
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

    fun loadScheduleById(id: Int) {
        viewModelScope.launch {
            repository.getScheduleByIdWithMedicine(id)
                .catch { e -> _selectedSchedule.value = null }
                .collect { _selectedSchedule.value = it }
        }
    }

    fun addSchedule(medicineId: Int, time: String) {
        viewModelScope.launch {
            val newMedicine = Schedule(time = time, medicineId = medicineId)
            repository.addSchedule(newMedicine)
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

    fun deleteSchedule(scheduleId: Int) {
        viewModelScope.launch {
            repository.deleteSchedule(scheduleId)
        }
    }
}
