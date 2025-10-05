package com.example.warasin.ui.healthnotes

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.warasin.data.preferences.UserPreferences
import com.example.warasin.data.repository.HealthNoteRepository
import com.example.warasin.data.model.HealthNote
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HealthNotesViewModel @Inject constructor(
    private val repository: HealthNoteRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val userId = userPreferences.getUserId()

    val healthNotes = repository.getHealthNotesByUserId(userId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )

    init {
        // Sync data both ways when ViewModel is created
        syncData()
    }

    private fun syncData() {
        viewModelScope.launch {
            try {
                // First, sync FROM Firestore TO Local
                repository.syncHealthNotesFromFirestore()

                // Then, sync unsynced local data TO Firestore
                repository.syncAllUnsyncedHealthNotes()
            } catch (e: Exception) {
                Log.e("HealthNotesViewModel", "Sync error: ${e.message}")
            }
        }
    }

    fun addHealthNote(
        bloodPressure: String,
        bloodSugar: String,
        bodyTemperature: String,
        mood: String,
        notes: String
    ) {
        viewModelScope.launch {
            repository.addHealthNote(bloodPressure, bloodSugar, bodyTemperature, mood, notes)
        }
    }

    fun updateHealthNote(healthNote: HealthNote) {
        viewModelScope.launch {
            repository.updateHealthNote(healthNote)
        }
    }

    fun deleteHealthNote(healthNote: HealthNote) {
        viewModelScope.launch {
            repository.deleteHealthNote(healthNote)
        }
    }

    fun forceSyncData() {
        syncData()
    }
}