package com.example.warasin.ui.healthnotes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.warasin.data.dao.HealthNoteDao
import com.example.warasin.data.model.HealthNote
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HealthNotesViewModel @Inject constructor(
    private val healthNoteDao: HealthNoteDao
) : ViewModel() {

    val healthNotes = healthNoteDao.getAllHealthNote()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )

    fun addHealthNote(bloodPressure: String, bloodSugar: String, bodyTemperature: String, mood: String, notes: String) {
        viewModelScope.launch {
            val healthNote = HealthNote(
                bloodPressure = bloodPressure,
                bloodSugar = bloodSugar,
                bodyTemperature = bodyTemperature,
                mood = mood,
                notes = notes,
            )
            healthNoteDao.insertHealthNote(healthNote)
        }
    }

    fun deleteHealthNote(healthNote: HealthNote) {
        viewModelScope.launch {
            healthNoteDao.deleteHealthNote(healthNote)
        }
    }

    fun updateHealthNote(healthNote: HealthNote) {
        viewModelScope.launch {
            healthNoteDao.insertHealthNote(healthNote) // `insert` dengan `onConflict` akan menggantikan data lama
        }
    }
}