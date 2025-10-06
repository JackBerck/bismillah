package com.example.warasin.data.repository

import android.util.Log
import com.example.warasin.data.DataStoreRepository
import com.example.warasin.data.preferences.UserPreferences
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val userPreferences: UserPreferences,
    private val dataStoreRepository: DataStoreRepository
) {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    suspend fun checkAuthState(): Boolean {
        val currentUser = auth.currentUser
        val isLoggedInDataStore = dataStoreRepository.readAuthState().first()
        val (userId, _, _) = dataStoreRepository.readUserData().first()

        Log.d("AuthRepository", "Firebase user: ${currentUser?.uid}")
        Log.d("AuthRepository", "DataStore logged in: $isLoggedInDataStore")
        Log.d("AuthRepository", "User ID from DataStore: $userId")

        return currentUser != null && isLoggedInDataStore && userId.isNotEmpty()
    }

    suspend fun getCurrentUserData(): Triple<String, String, String> {
        return dataStoreRepository.readUserData().first()
    }

    suspend fun saveUserData(userId: String, userName: String, userEmail: String) {
        Log.d("AuthRepository", "Saving user data - ID: $userId, Name: $userName, Email: $userEmail")

        // Save to both DataStore and SharedPreferences
        dataStoreRepository.saveAuthState(true, userId, userName, userEmail)
        userPreferences.saveUserData(userId, userName, userEmail)

        Log.d("AuthRepository", "User data saved successfully")
    }

    suspend fun logout() {
        Log.d("AuthRepository", "Logging out user")
        auth.signOut()
        dataStoreRepository.clearAuthState()
        userPreferences.clearUserData()
    }
}