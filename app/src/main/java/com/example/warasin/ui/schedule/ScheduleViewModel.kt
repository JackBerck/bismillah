package com.example.warasin.ui.schedule

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.warasin.data.model.Medicine
import com.example.warasin.data.model.MedicineWithSchedules
import com.example.warasin.data.model.Schedule
import com.example.warasin.data.model.ScheduleWithMedicine
import com.example.warasin.data.repository.MedicineRepository
import com.example.warasin.notification.AlarmScheduler
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val repository: MedicineRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val _schedules = MutableStateFlow<List<ScheduleWithMedicine>>(emptyList())
    val schedules: StateFlow<List<ScheduleWithMedicine>> = _schedules.asStateFlow()

    private val _medicines = MutableStateFlow<List<MedicineWithSchedules>>(emptyList())
    val medicines: StateFlow<List<MedicineWithSchedules>> = _medicines.asStateFlow()

    private val _selectedSchedule = MutableStateFlow<ScheduleWithMedicine?>(null)
    val selectedSchedule: StateFlow<ScheduleWithMedicine?> = _selectedSchedule.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val alarmScheduler = AlarmScheduler(context)

    init {
        loadAllSchedule()
        loadAllMedicines()
        syncDataFromFirestore()
    }

    private fun loadAllSchedule() {
        viewModelScope.launch {
            repository.getAllSchedules()
                .catch { exception ->
                    println("Error loading schedules: ${exception.message}")
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

    private fun syncDataFromFirestore() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.syncAllFromFirestore()
                repository.syncAllUnsyncedData()
            } catch (e: Exception) {
                println("Sync error: ${e.message}")
            } finally {
                _isLoading.value = false
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

    fun addSchedule(medicineId: Int, time: String, selectedDays: List<Int>) {
        viewModelScope.launch {
            val scheduleId = repository.addSchedule(medicineId, time, selectedDays)

            repository.getScheduleByIdWithMedicine(scheduleId.toInt())
                .collect { scheduleWithMedicine ->
                    scheduleWithMedicine?.let {
                        alarmScheduler.schedule(it)
                    }
                }
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
            repository.getScheduleByIdWithMedicine(scheduleId)
                .collect { scheduleWithMedicine ->
                    scheduleWithMedicine?.let {
                        alarmScheduler.cancel(it)
                    }
                    repository.deleteSchedule(scheduleId)
                }
        }
    }

    fun updateSchedule(schedule: Schedule) {
        viewModelScope.launch {
            repository.updateSchedule(schedule)

            repository.getScheduleByIdWithMedicine(schedule.id)
                .collect { scheduleWithMedicine ->
                    scheduleWithMedicine?.let {
                        alarmScheduler.schedule(it)
                    }
                }
        }
    }

    fun refreshData() {
        syncDataFromFirestore()
    }
}