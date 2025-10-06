package com.example.warasin.ui.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.warasin.data.repository.AuthRepository
import com.example.warasin.data.repository.HealthNoteRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.AuthCredential
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val healthNoteRepository: HealthNoteRepository
) : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _uiState = MutableStateFlow(AuthState())
    val uiState = _uiState.asStateFlow()

    fun getCurrentUserData(): Pair<String, String> {
        val firebaseUser = auth.currentUser
        return if (firebaseUser != null) {
            val uid = firebaseUser.uid
            val displayName = firebaseUser.displayName ?: "" // Get display name, or empty string if null
            Pair(uid, displayName)
        } else {
            Pair("", "") // Return empty strings if no user is logged in
        }
    }

    fun onEvent(event: AuthEvent) {
        when (event) {
            is AuthEvent.OnFullNameChange -> {
                _uiState.update { it.copy(fullName = event.value) }
            }
            is AuthEvent.OnEmailChange -> {
                _uiState.update { it.copy(email = event.value) }
            }
            is AuthEvent.OnPasswordChange -> {
                _uiState.update { it.copy(password = event.value) }
            }
            is AuthEvent.OnConfirmPasswordChange -> {
                _uiState.update { it.copy(confirmPassword = event.value) }
            }
            AuthEvent.ToggleShowPassword -> {
                _uiState.update { it.copy(showPassword = !it.showPassword) }
            }
            AuthEvent.ToggleShowConfirmPassword -> {
                _uiState.update { it.copy(showConfirmPassword = !it.showConfirmPassword) }
            }
            is AuthEvent.OnTermsAgreeChange -> {
                _uiState.update { it.copy(agreedToTerms = event.value) }
            }
            AuthEvent.RegisterClick -> {
                registerUser()
            }
            AuthEvent.LoginClick -> {
                loginWithEmail()
            }
            AuthEvent.ErrorShown -> {
                _uiState.update { it.copy(registrationError = null) }
            }
        }
    }

    private fun registerUser() {
        val currentState = _uiState.value
        val fullName = currentState.fullName.trim()
        val email = currentState.email.trim()
        val password = currentState.password.trim()

        if (fullName.isEmpty() || email.isEmpty() || password.isEmpty() || currentState.confirmPassword.isEmpty()) {
            _uiState.update { it.copy(registrationError = "Semua field harus diisi.") }
            return
        }
        if (password != currentState.confirmPassword) {
            _uiState.update { it.copy(registrationError = "Password tidak cocok.") }
            return
        }
        if (!currentState.agreedToTerms) {
            _uiState.update { it.copy(registrationError = "Anda harus menyetujui syarat & ketentuan.") }
            return
        }

        _uiState.update { it.copy(isLoading = true, registrationError = null) }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val profileUpdates = UserProfileChangeRequest.Builder().setDisplayName(fullName).build()
                    user?.updateProfile(profileUpdates)?.addOnCompleteListener {
                        // Save user data menggunakan AuthRepository
                        user.let {
                            // Direct call ke UserPreferences melalui AuthRepository
                            viewModelScope.launch {
                                // Simpan data user
                                // AuthRepository akan handle ini
                                _uiState.update { it.copy(isLoading = false, registrationSuccess = true) }
                            }
                        }
                    }
                } else {
                    _uiState.update { it.copy(isLoading = false, registrationError = task.exception?.message) }
                }
            }
    }

    private fun loginWithEmail() {
        val currentState = _uiState.value
        val email = currentState.email.trim()
        val password = currentState.password.trim()

        if (email.isEmpty() || password.isEmpty()) {
            _uiState.update { it.copy(registrationError = "Email dan password tidak boleh kosong.") }
            return
        }

        _uiState.update { it.copy(isLoading = true, registrationError = null) }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    user?.let {
                        viewModelScope.launch {
                            try {
                                // Sync user data to Firestore
                                healthNoteRepository.syncUserToFirestore(
                                    userId = it.uid,
                                    name = it.displayName ?: "",
                                    email = it.email ?: ""
                                )

                                // Sync health notes from Firestore after login
                                healthNoteRepository.syncHealthNotesFromFirestore()

                                _uiState.update { state ->
                                    state.copy(isLoading = false, registrationSuccess = true)
                                }
                            } catch (e: Exception) {
                                Log.e("AuthViewModel", "Sync error after login: ${e.message}")
                                _uiState.update { state ->
                                    state.copy(isLoading = false, registrationSuccess = true)
                                }
                            }
                        }
                    }
                } else {
                    _uiState.update { it.copy(isLoading = false, registrationError = task.exception?.message) }
                }
            }
    }

    fun signInWithGoogleCredential(credential: AuthCredential) {
        _uiState.update { it.copy(isLoading = true, registrationError = null) }
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _uiState.update { it.copy(isLoading = false, registrationSuccess = true) }
                } else {
                    _uiState.update { it.copy(isLoading = false, registrationError = task.exception?.message) }
                }
            }
    }

    fun logout() {
        authRepository.logout()
    }
}