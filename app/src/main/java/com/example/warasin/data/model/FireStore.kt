package com.example.warasin.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

data class FirestoreUser(
    @DocumentId
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val phone_number: String = "",
    val age: Int = 0
)

data class FirestoreHealthNote(
    @DocumentId
    val id: String = "",
    val userId: String = "",
    val bloodPressure: String = "",
    val bloodSugar: Int = 0,
    val bodyTemperature: Int = 0,
    val mood: String = "",
    val notes: String = "",
    val timestamp: Timestamp = Timestamp.now(),
    val isSynced: Boolean = false // untuk tracking sync status
)

data class FirestoreMedicine(
    @DocumentId
    val id: String = "",
    val userId: String = "",
    val name: String = "",
    val dosage: String = "",
    val notes: String = "",
    val timestamp: Timestamp = Timestamp.now(),
    val isSynced: Boolean = false
)

data class FirestoreSchedule(
    @DocumentId
    val id: String = "",
    val userId: String = "",
    val medicineId: String = "", // Reference to Firestore medicine ID
    val time: String = "",
    val selectedDays: String = "",
    val isTaken: Boolean = false,
    val timestamp: Timestamp = Timestamp.now(),
    val isSynced: Boolean = false
)