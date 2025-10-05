package com.example.warasin.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.google.firebase.auth.AuthCredential

@HiltViewModel
class AuthViewModel @Inject constructor() : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _uiState = MutableStateFlow(AuthState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(event: AuthEvent) {
        when (event) {
            is AuthEvent.OnFullNameChange -> _uiState.update { it.copy(fullName = event.value) }
            is AuthEvent.OnEmailChange -> _uiState.update { it.copy(email = event.value) }
            is AuthEvent.OnPasswordChange -> _uiState.update { it.copy(password = event.value) }
            is AuthEvent.OnConfirmPasswordChange -> _uiState.update { it.copy(confirmPassword = event.value) }
            is AuthEvent.ToggleShowPassword -> _uiState.update { it.copy(showPassword = !it.showPassword) }
            is AuthEvent.ToggleShowConfirmPassword -> _uiState.update { it.copy(showConfirmPassword = !it.showConfirmPassword) }
            is AuthEvent.OnTermsAgreeChange -> _uiState.update { it.copy(agreedToTerms = event.value) }
            is AuthEvent.RegisterClick -> registerUser()
            is AuthEvent.ErrorShown -> _uiState.update { it.copy(registrationError = null) }
            is AuthEvent.LoginClick -> loginWithEmail()
            is AuthEvent.ErrorShown -> _uiState.update { it.copy(registrationError = null) }
        }
    }

    private fun registerUser() {
        // Ambil state saat ini
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
                        _uiState.update { it.copy(isLoading = false, registrationSuccess = true) }
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
                    _uiState.update { it.copy(isLoading = false, registrationSuccess = true) }
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
}