package com.friendspark.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

actual class FirebaseAuthManager actual constructor() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    actual suspend fun createUser(email: String, password: String): Result<Unit> = runCatching {
        auth.createUserWithEmailAndPassword(email, password).await()
    }

    actual suspend fun signIn(email: String, password: String): Result<Unit> = runCatching {
        auth.signInWithEmailAndPassword(email, password).await()
    }

    actual suspend fun getIdToken(): String? {
        val user: FirebaseUser? = auth.currentUser
        return user?.getIdToken(false)?.await()?.token
    }
}
