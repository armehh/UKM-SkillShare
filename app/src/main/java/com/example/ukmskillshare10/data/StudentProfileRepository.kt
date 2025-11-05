package com.example.ukmskillshare10.data

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class StudentProfileRepository(
    private val firestore: FirebaseFirestore,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    private val collection = firestore.collection(COLLECTION_NAME)

    suspend fun saveProfile(uid: String, profile: StudentProfile) {
        withContext(ioDispatcher) {
            collection.document(uid).set(profile).await()
        }
    }

    suspend fun loadProfile(uid: String): StudentProfile? = withContext(ioDispatcher) {
        collection.document(uid).get().await().toObject(StudentProfile::class.java)
    }

    fun observeProfile(uid: String): Flow<StudentProfile?> = callbackFlow {
        val registration = collection.document(uid).addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            trySend(snapshot?.toObject(StudentProfile::class.java)).isSuccess
        }

        awaitClose { registration.remove() }
    }

    companion object {
        private const val COLLECTION_NAME = "studentProfiles"
    }
}

