package com.example.warasin.data

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "waras_in_settings")

class DataStoreRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private object PreferencesKey {
        val onBoardingKey = booleanPreferencesKey(name = "on_boarding_completed")
        val isLoggedInKey = booleanPreferencesKey(name = "is_logged_in")
        val userIdKey = stringPreferencesKey(name = "user_id")
        val userNameKey = stringPreferencesKey(name = "user_name")
        val userEmailKey = stringPreferencesKey(name = "user_email")
    }

    // Onboarding
    suspend fun saveOnBoardingState(completed: Boolean) {
        Log.d("DataStoreRepository", "Saving onboarding state: $completed")
        context.dataStore.edit { preferences ->
            preferences[PreferencesKey.onBoardingKey] = completed
        }
        Log.d("DataStoreRepository", "Onboarding state saved")
    }

    fun readOnBoardingState(): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            val state = preferences[PreferencesKey.onBoardingKey] ?: false
            Log.d("DataStoreRepository", "Reading onboarding state: $state")
            state
        }
    }

    // Auth State
    suspend fun saveAuthState(isLoggedIn: Boolean, userId: String = "", userName: String = "", userEmail: String = "") {
        Log.d("DataStoreRepository", "Saving auth state - LoggedIn: $isLoggedIn, UserID: $userId, Name: $userName, Email: $userEmail")
        context.dataStore.edit { preferences ->
            preferences[PreferencesKey.isLoggedInKey] = isLoggedIn
            preferences[PreferencesKey.userIdKey] = userId
            preferences[PreferencesKey.userNameKey] = userName
            preferences[PreferencesKey.userEmailKey] = userEmail
        }
        Log.d("DataStoreRepository", "Auth state saved to DataStore")
    }

    fun readAuthState(): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            val state = preferences[PreferencesKey.isLoggedInKey] ?: false
            Log.d("DataStoreRepository", "Reading auth state: $state")
            state
        }
    }

    fun readUserData(): Flow<Triple<String, String, String>> {
        return context.dataStore.data.map { preferences ->
            val userId = preferences[PreferencesKey.userIdKey] ?: ""
            val userName = preferences[PreferencesKey.userNameKey] ?: ""
            val userEmail = preferences[PreferencesKey.userEmailKey] ?: ""
            Log.d("DataStoreRepository", "Reading user data - ID: '$userId', Name: '$userName', Email: '$userEmail'")
            Triple(userId, userName, userEmail)
        }
    }

    suspend fun clearAuthState() {
        Log.d("DataStoreRepository", "Clearing auth state")
        context.dataStore.edit { preferences ->
            preferences[PreferencesKey.isLoggedInKey] = false
            preferences[PreferencesKey.userIdKey] = ""
            preferences[PreferencesKey.userNameKey] = ""
            preferences[PreferencesKey.userEmailKey] = ""
        }
        Log.d("DataStoreRepository", "Auth state cleared from DataStore")
    }

    // Debug method
    suspend fun debugAllData() {
        context.dataStore.data.map { preferences ->
            Log.d("DataStoreRepository", "=== DATASTORE DEBUG ===")
            Log.d("DataStoreRepository", "Onboarding: ${preferences[PreferencesKey.onBoardingKey]}")
            Log.d("DataStoreRepository", "Is logged in: ${preferences[PreferencesKey.isLoggedInKey]}")
            Log.d("DataStoreRepository", "User ID: '${preferences[PreferencesKey.userIdKey]}'")
            Log.d("DataStoreRepository", "User name: '${preferences[PreferencesKey.userNameKey]}'")
            Log.d("DataStoreRepository", "User email: '${preferences[PreferencesKey.userEmailKey]}'")
        }
    }
}