package com.example.warasin.ui.homepage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.warasin.data.model.HealthNote
import com.example.warasin.data.model.ScheduleWithMedicine
import com.example.warasin.data.preferences.UserPreferences
import com.example.warasin.data.repository.HealthNoteRepository
import com.example.warasin.data.repository.MedicineRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomepageViewModel @Inject constructor(
    private val medicineRepository: MedicineRepository,
    private val healthNoteRepository: HealthNoteRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _schedules = MutableStateFlow<List<ScheduleWithMedicine>>(emptyList())
    val schedules: StateFlow<List<ScheduleWithMedicine>> = _schedules.asStateFlow()
    val userId = userPreferences.getUserId()

    private val _healthNotes = MutableStateFlow<List<HealthNote>>(emptyList())
    val healthNotes: StateFlow<List<HealthNote>> = _healthNotes.asStateFlow()

    init {
        loadAllSchedules()
        loadRecentHealthNotes()
    }

    private fun loadAllSchedules() {
        viewModelScope.launch {
            medicineRepository.getAllSchedules()
                .catch { exception ->
                    println("Error loading schedules: ${exception.message}")
                }
                .collect { scheduleList ->
                    _schedules.value = scheduleList
                }
        }
    }

    private fun loadRecentHealthNotes() {
        viewModelScope.launch {
            healthNoteRepository.getHealthNotesByUserId(userId)
                .catch { exception ->
                    println("Error loading health notes: ${exception.message}")
                }
                .collect { healthNotesList ->
                    _healthNotes.value = healthNotesList.take(1)
                }
        }
    }

    fun markScheduleAsTaken(scheduleId: Int) {
        viewModelScope.launch {
            medicineRepository.updateScheduleStatus(scheduleId, true)
        }
    }

    fun markScheduleAsNotTaken(scheduleId: Int) {
        viewModelScope.launch {
            medicineRepository.updateScheduleStatus(scheduleId, false)
        }
    }
}