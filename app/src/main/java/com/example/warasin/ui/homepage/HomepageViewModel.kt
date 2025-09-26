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
    private val medicineDao: MedicineDao
): ViewModel() {

    val medicines = medicineDao.getAllMedicines()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )

    fun markMedicineAsTaken(medicineId: Int) {
        viewModelScope.launch {
            medicineDao.updateMedicineTaken(medicineId, true)
        }
    }

    fun markMedicineAsNotTaken(medicineId: Int) {
        viewModelScope.launch {
            medicineDao.updateMedicineTaken(medicineId, false)
        }
    }
}