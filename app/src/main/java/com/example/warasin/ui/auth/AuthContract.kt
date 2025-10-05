package com.example.warasin.ui.auth

data class AuthState(
    val fullName: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val showPassword: Boolean = false,
    val showConfirmPassword: Boolean = false,
    val agreedToTerms: Boolean = false,
    val isLoading: Boolean = false,
    val registrationError: String? = null,
    val registrationSuccess: Boolean = false
)

sealed interface AuthEvent {
    data class OnFullNameChange(val value: String) : AuthEvent
    data class OnEmailChange(val value: String) : AuthEvent
    data class OnPasswordChange(val value: String) : AuthEvent
    data class OnConfirmPasswordChange(val value: String) : AuthEvent
    object ToggleShowPassword : AuthEvent
    object ToggleShowConfirmPassword : AuthEvent
    data class OnTermsAgreeChange(val value: Boolean) : AuthEvent
    object RegisterClick : AuthEvent
    object ErrorShown : AuthEvent
    object LoginClick : AuthEvent
}