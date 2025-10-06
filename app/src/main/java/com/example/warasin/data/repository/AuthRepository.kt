package com.example.warasin.data.repository

import com.example.warasin.data.preferences.UserPreferences
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val userPreferences: UserPreferences
) {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun checkAuthState(): Boolean {
        val currentUser = auth.currentUser
        val isLoggedInPrefs = userPreferences.isLoggedIn()

        return currentUser != null && isLoggedInPrefs
    }

    fun getCurrentUserData(): Triple<String, String, String> {
        return Triple(
            userPreferences.getUserId(),
            userPreferences.getUserName(),
            userPreferences.getUserEmail()
        )
    }

    fun logout() {
        auth.signOut()
        userPreferences.clearUserData()
    }
}