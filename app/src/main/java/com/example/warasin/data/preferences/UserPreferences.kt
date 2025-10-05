package com.example.warasin.data.preferences

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val prefs: SharedPreferences = context.getSharedPreferences(
        "user_preferences",
        Context.MODE_PRIVATE
    )

    companion object {
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_USER_AGE = "user_age"
        private const val KEY_USER_PHONE = "user_phone"
        private const val KEY_PROFILE_IMAGE_URI = "profile_image_uri"
    }

    fun saveUserData(userId: String, userName: String, userEmail: String) {
        prefs.edit().apply {
            putBoolean(KEY_IS_LOGGED_IN, true)
            putString(KEY_USER_ID, userId)
            putString(KEY_USER_NAME, userName)
            putString(KEY_USER_EMAIL, userEmail)
            apply()
        }
    }

    fun updateUserProfile(name: String, age: String, phoneNumber: String, profileImageUri: String) {
        prefs.edit().apply {
            putString(KEY_USER_NAME, name)
            putString(KEY_USER_AGE, age)
            putString(KEY_USER_PHONE, phoneNumber)
            putString(KEY_PROFILE_IMAGE_URI, profileImageUri)
            apply()
        }
    }

    fun isLoggedIn(): Boolean = prefs.getBoolean(KEY_IS_LOGGED_IN, false)

    fun getUserId(): String = prefs.getString(KEY_USER_ID, "") ?: ""

    fun getUserName(): String = prefs.getString(KEY_USER_NAME, "") ?: ""

    fun getUserEmail(): String = prefs.getString(KEY_USER_EMAIL, "") ?: ""

    fun getUserAge(): String = prefs.getString(KEY_USER_AGE, "") ?: ""

    fun getUserPhoneNumber(): String = prefs.getString(KEY_USER_PHONE, "") ?: ""

    fun getProfileImageUri(): String = prefs.getString(KEY_PROFILE_IMAGE_URI, "") ?: ""

    fun clearUserData() {
        prefs.edit().clear().apply()
    }
}