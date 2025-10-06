package com.example.warasin.ui.profile

import android.net.Uri
import android.util.Log // <-- Import Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.warasin.data.preferences.UserPreferences
import com.example.warasin.data.repository.AuthRepository
import com.example.warasin.data.repository.HealthNoteRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileState(
    val name: String = "",
    val email: String = "",
    val age: String = "",
    val phoneNumber: String = "",
    val profileImageUri: String = "",
    val isEditMode: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userPreferences: UserPreferences,
    private val repository: HealthNoteRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileState())
    val uiState = _uiState.asStateFlow()

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        val (userId, userName, userEmail) = authRepository.getCurrentUserData()
        _uiState.update {
            it.copy(
                name = userPreferences.getUserName(),
                email = userPreferences.getUserEmail(),
                age = userPreferences.getUserAge(),
                phoneNumber = userPreferences.getUserPhoneNumber(),
                profileImageUri = userPreferences.getProfileImageUri()
            )
        }
    }

    fun toggleEditMode() {
        _uiState.update { it.copy(isEditMode = !it.isEditMode) }
    }

    fun updateName(name: String) {
        _uiState.update { it.copy(name = name) }
    }

    fun updateAge(age: String) {
        _uiState.update { it.copy(age = age) }
    }

    fun updatePhoneNumber(phoneNumber: String) {
        _uiState.update { it.copy(phoneNumber = phoneNumber) }
    }

    fun updateProfileImage(uri: Uri) {
        _uiState.update { it.copy(profileImageUri = uri.toString()) }
    }

    fun saveProfile() {
        val currentState = _uiState.value

        if (currentState.name.trim().isEmpty()) {
            _uiState.update { it.copy(errorMessage = "Nama tidak boleh kosong") }
            return
        }

        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            try {
                // Save to SharedPreferences
                userPreferences.updateUserProfile(
                    name = currentState.name.trim(),
                    age = currentState.age.trim(),
                    phoneNumber = currentState.phoneNumber.trim(),
                    profileImageUri = currentState.profileImageUri
                )

                // Sync to Firestore
                repository.syncUserToFirestore(
                    userId = userPreferences.getUserId(),
                    name = currentState.name.trim(),
                    email = currentState.email,
                    age = currentState.age.trim().toIntOrNull() ?: 0,
                    phoneNumber = currentState.phoneNumber.trim()
                )

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isEditMode = false,
                        errorMessage = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Gagal menyimpan profile: ${e.message}"
                    )
                }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    fun logout() {
        viewModelScope.launch {
            try {
                authRepository.logout()
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Logout error: ${e.message}")
            }
        }
    }
}
