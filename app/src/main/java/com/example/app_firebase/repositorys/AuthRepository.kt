package com.example.app_firebase.repositorys

import com.example.app_firebase.models.User
import com.example.app_firebase.models.states.UiState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AuthRepository {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    fun register(
        name: String,
        email: String,
        password: String,
        callback: (UiState<String>) -> Unit
    ) {

        callback(UiState.Loading)

        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                val uid = result.user?.uid
                if (uid == null) {
                    callback(UiState.Error("UID is null"))
                    return@addOnSuccessListener
                }

                val user = User(uid, name, email)

                db.collection("users")
                    .document(uid)
                    .set(user)
                    .addOnSuccessListener {
                        callback(UiState.Success("Register Success"))
                    }
                    .addOnFailureListener {
                        callback(UiState.Error(it.message ?: "Error Register Repo"))
                    }
            }
            .addOnFailureListener {
                callback(UiState.Error(it.message ?: "Error Register Repo"))
            }

    }

    fun login(
        email: String,
        password: String,
        callback: (UiState<String>) -> Unit
    ) {
        callback(UiState.Loading)

        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                callback(UiState.Success("Login Success"))
            }
            .addOnFailureListener {
                callback(UiState.Error(it.message ?: "Error Login Repo"))
            }
    }

}