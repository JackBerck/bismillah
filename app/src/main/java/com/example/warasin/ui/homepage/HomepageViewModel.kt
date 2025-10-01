package com.example.warasin.ui.homepage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.warasin.data.dao.MedicineDao
import com.example.warasin.data.model.Medicine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomepageViewModel @Inject constructor(
    private val scheduleDao: MedicineDao,
): ViewModel() {
    val schedules = scheduleDao.getSchedulesWithMedicine()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )

    fun markMedicineAsTaken(medicineId: Int) {
        viewModelScope.launch {
            scheduleDao.updateScheduleTakenStatus(medicineId, true)
        }
    }

    fun markMedicineAsNotTaken(medicineId: Int) {
        viewModelScope.launch {
            scheduleDao.updateScheduleTakenStatus(medicineId, false)
        }
    }
}