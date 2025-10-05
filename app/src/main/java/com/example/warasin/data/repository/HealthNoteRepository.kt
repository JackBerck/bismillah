package com.example.warasin.data.repository

import android.util.Log
import com.example.warasin.data.dao.HealthNoteDao
import com.example.warasin.data.model.HealthNote
import com.example.warasin.data.model.FirestoreHealthNote
import com.example.warasin.data.model.FirestoreUser
import com.example.warasin.data.preferences.UserPreferences
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HealthNoteRepository @Inject constructor(
    private val healthNoteDao: HealthNoteDao,
    private val userPreferences: UserPreferences,
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    fun getHealthNotesByUserId(userId: String): Flow<List<HealthNote>> {
        return healthNoteDao.getHealthNotesByUserId(userId)
    }

    // Tambahkan fungsi untuk sync FROM Firestore TO Local
    suspend fun syncHealthNotesFromFirestore() {
        try {
            val userId = userPreferences.getUserId()
            if (userId.isEmpty()) return

            Log.d("HealthNoteRepository", "Fetching health notes from Firestore for user: $userId")

            val firestoreNotes = firestore.collection("health_notes")
                .whereEqualTo("userId", userId)
                .get()
                .await()

            Log.d("HealthNoteRepository", "Found ${firestoreNotes.documents.size} notes in Firestore")

            firestoreNotes.documents.forEach { document ->
                try {
                    val firestoreNote = document.toObject(FirestoreHealthNote::class.java)
                    firestoreNote?.let { note ->
                        // Convert Firestore note to local HealthNote
                        val localHealthNote = HealthNote(
                            userId = note.userId,
                            bloodPressure = note.bloodPressure,
                            bloodSugar = note.bloodSugar.toString(),
                            bodyTemperature = note.bodyTemperature.toString(),
                            mood = note.mood,
                            notes = note.notes,
                            timestamp = note.timestamp.toDate().time,
                            isSynced = true,
                            firestoreId = document.id
                        )

                        // Check if note already exists locally
                        val existingNote = healthNoteDao.getHealthNoteByFirestoreId(document.id)
                        if (existingNote == null) {
                            healthNoteDao.insertHealthNote(localHealthNote)
                            Log.d("HealthNoteRepository", "Inserted new health note from Firestore: ${document.id}")
                        }
                    }
                } catch (e: Exception) {
                    Log.e("HealthNoteRepository", "Error processing Firestore document ${document.id}: ${e.message}")
                }
            }

            Log.d("HealthNoteRepository", "Health notes sync from Firestore completed")
        } catch (e: Exception) {
            Log.e("HealthNoteRepository", "Failed to sync health notes from Firestore: ${e.message}")
        }
    }

    // Existing functions remain the same...
    suspend fun addHealthNote(
        bloodPressure: String,
        bloodSugar: String,
        bodyTemperature: String,
        mood: String,
        notes: String
    ) {
        val userId = userPreferences.getUserId()
        val healthNote = HealthNote(
            userId = userId,
            bloodPressure = bloodPressure,
            bloodSugar = bloodSugar,
            bodyTemperature = bodyTemperature,
            mood = mood,
            notes = notes,
            isSynced = false
        )

        // Save to local database first
        val localId = healthNoteDao.insertHealthNote(healthNote)

        // Try to sync to Firestore
        try {
            syncHealthNoteToFirestore(healthNote.copy(id = localId.toInt()))
            Log.d("HealthNoteRepository", "Health note added and synced successfully")
        } catch (e: Exception) {
            Log.d("HealthNoteRepository", "Failed to sync immediately: ${e.message}")
        }
    }

    suspend fun updateHealthNote(healthNote: HealthNote) {
        // Update local database
        healthNoteDao.updateHealthNote(healthNote.copy(isSynced = false))

        // Try to sync to Firestore
        try {
            syncHealthNoteToFirestore(healthNote)
        } catch (e: Exception) {
            Log.d("HealthNoteRepository", "Failed to sync update immediately: ${e.message}")
        }
    }

    suspend fun deleteHealthNote(healthNote: HealthNote) {
        // Delete from local database
        healthNoteDao.deleteHealthNote(healthNote)

        // Delete from Firestore if synced
        try {
            if (healthNote.firestoreId != null) {
                firestore.collection("health_notes")
                    .document(healthNote.firestoreId)
                    .delete()
                    .await()
            }
        } catch (e: Exception) {
            Log.d("HealthNoteRepository", "Failed to delete from Firestore: ${e.message}")
        }
    }

    suspend fun syncHealthNoteToFirestore(healthNote: HealthNote) {
        try {
            val firestoreHealthNote = FirestoreHealthNote(
                userId = healthNote.userId,
                bloodPressure = healthNote.bloodPressure,
                bloodSugar = healthNote.bloodSugar.toIntOrNull() ?: 0,
                bodyTemperature = healthNote.bodyTemperature.toIntOrNull() ?: 0,
                mood = healthNote.mood,
                notes = healthNote.notes,
                timestamp = Timestamp(java.util.Date(healthNote.timestamp)),
                isSynced = true
            )

            if (healthNote.firestoreId != null) {
                // Update existing document
                firestore.collection("health_notes")
                    .document(healthNote.firestoreId)
                    .set(firestoreHealthNote)
                    .await()
                healthNoteDao.markAsSynced(healthNote.id, healthNote.firestoreId)
            } else {
                // Create new document
                val documentRef = firestore.collection("health_notes").add(firestoreHealthNote).await()
                healthNoteDao.markAsSynced(healthNote.id, documentRef.id)
            }

            Log.d("HealthNoteRepository", "Health note synced successfully")
        } catch (e: Exception) {
            Log.e("HealthNoteRepository", "Failed to sync health note: ${e.message}")
            throw e
        }
    }

    suspend fun syncAllUnsyncedHealthNotes() {
        try {
            val userId = userPreferences.getUserId()
            val unsyncedNotes = healthNoteDao.getUnsyncedHealthNotes(userId)

            unsyncedNotes.forEach { healthNote ->
                try {
                    syncHealthNoteToFirestore(healthNote)
                } catch (e: Exception) {
                    Log.e("HealthNoteRepository", "Failed to sync note ${healthNote.id}: ${e.message}")
                }
            }
        } catch (e: Exception) {
            Log.e("HealthNoteRepository", "Failed to sync unsynced health notes: ${e.message}")
        }
    }

    suspend fun syncUserToFirestore(userId: String, name: String, email: String) {
        try {
            val firestoreUser = FirestoreUser(
                id = userId,
                name = name,
                email = email,
                password = "",
                phone_number = "",
                age = 0
            )

            firestore.collection("users")
                .document(userId)
                .set(firestoreUser)
                .await()

            Log.d("HealthNoteRepository", "User synced successfully")
        } catch (e: Exception) {
            Log.e("HealthNoteRepository", "Failed to sync user: ${e.message}")
        }
    }
}