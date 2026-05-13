package com.example.sareevastra.auth

import com.google.firebase.auth.FirebaseAuth

object FirebaseAuthManager {

    private val auth =
        FirebaseAuth.getInstance()

    fun login(

        email: String,

        password: String,

        onSuccess: () -> Unit,

        onError: (String) -> Unit
    ) {

        auth.signInWithEmailAndPassword(

            email,

            password

        ).addOnSuccessListener {

            onSuccess()

        }.addOnFailureListener {

            onError(
                it.message ?: "Login Failed"
            )
        }
    }

    fun signup(

        email: String,

        password: String,

        onSuccess: () -> Unit,

        onError: (String) -> Unit
    ) {

        auth.createUserWithEmailAndPassword(

            email,

            password

        ).addOnSuccessListener {

            onSuccess()

        }.addOnFailureListener {

            onError(
                it.message ?: "Signup Failed"
            )
        }
    }
}