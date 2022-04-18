package org.mtali.podmoji.data

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.withContext
import org.mtali.podmoji.api.State
import org.mtali.podmoji.models.User
import timber.log.Timber

class UsersRepo {
    suspend fun getUsers(): Flow<State<List<User>>> = callbackFlow {
        val db = FirebaseFirestore.getInstance()
        val usersRef = db.collection("users")
        val subscription = usersRef
            .orderBy("emojiUpdatedAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error == null) {
                    val users = snapshot!!.documents.map {
                        User(
                            id = it.id,
                            displayName = it.get("displayName") as String,
                            emojis = it.get("emojis") as String,
                            emojiUpdatedAt = it.getLong("emojiUpdatedAt") ?: -1
                        )
                    }
                    trySend(State.success(users))
                }
            }

        awaitClose {
            Timber.i("Close FireStore subscription to retrieve users")
            subscription.remove()
        }
    }

    suspend fun saveEmojis(emojis: String) {
        withContext(Dispatchers.IO) {
            Firebase.auth.currentUser?.let { user ->
                val db = FirebaseFirestore.getInstance()
                db.collection("users").document(user.uid)
                    .update("emojis", emojis, "emojiUpdatedAt", System.currentTimeMillis())
                    .addOnSuccessListener {
                        Timber.d("Emoji updated success")
                    }
                    .addOnFailureListener {
                        Timber.d("Emoji update failed")
                    }

            }
        }
    }
}