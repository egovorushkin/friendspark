package com.friendspark.di

import com.friendspark.util.AppLogger
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

actual class FirebaseAuthManager actual constructor() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    actual suspend fun createUser(email: String, password: String): Result<Unit> = runCatching {
        AppLogger.logFirebase("Creating Firebase user with email: ${email.take(3)}***")
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        AppLogger.logFirebase("Firebase user created: uid=${result.user?.uid}")
        // Return Unit to match Result<Unit> return type
    }.onFailure { throwable ->
        val errorCode = (throwable as? FirebaseAuthException)?.errorCode
        val errorMessage = throwable.message ?: throwable.toString()
        AppLogger.logFirebaseError(
            "Failed to create Firebase user: errorCode=$errorCode, message=$errorMessage",
            throwable
        )

        // Re-throw with error code information for better error parsing
        if (throwable is FirebaseAuthException) {
            throw Exception("${throwable.errorCode}: ${throwable.message}")
        } else {
            throw throwable
        }
    }

    actual suspend fun signIn(email: String, password: String): Result<Unit> = runCatching {
        AppLogger.logFirebase("Signing in Firebase user with email: ${email.take(3)}***")
        val result = auth.signInWithEmailAndPassword(email, password).await()
        AppLogger.logFirebase("Firebase sign in successful: uid=${result.user?.uid}")
        // Return Unit to match Result<Unit> return type
    }.onFailure { throwable ->
        val errorCode = (throwable as? FirebaseAuthException)?.errorCode
        val errorMessage = throwable.message ?: throwable.toString()
        AppLogger.logFirebaseError(
            "Failed to sign in Firebase user: errorCode=$errorCode, message=$errorMessage",
            throwable
        )

        // Re-throw with error code information for better error parsing
        if (throwable is FirebaseAuthException) {
            throw Exception("${throwable.errorCode}: ${throwable.message}")
        } else {
            throw throwable
        }
    }

    actual suspend fun getIdToken(forceRefresh: Boolean): String? {
        val user: FirebaseUser? = auth.currentUser
        if (user == null) {
            AppLogger.logFirebaseError("No current user available to get token")
            return null
        }

        AppLogger.logFirebase("Getting ID token (forceRefresh=$forceRefresh) for user: ${user.uid}")
        return try {
            val token = user.getIdToken(forceRefresh).await().token
            AppLogger.logFirebase("ID token retrieved successfully (length: ${token?.length ?: 0})")
            token
        } catch (e: Exception) {
            AppLogger.logFirebaseError("Failed to get ID token: ${e.message}", e)
            null
        }
    }

    actual suspend fun refreshToken(): Result<Unit> = runCatching {
        val user: FirebaseUser? = auth.currentUser
        if (user == null) {
            AppLogger.logFirebaseError("No current user available to refresh token")
            throw Exception("No user to refresh token")
        }

        AppLogger.logFirebase("Refreshing token for user: ${user.uid}")
        user.getIdToken(true).await()
        AppLogger.logFirebase("Token refreshed successfully")
        // Return Unit to match Result<Unit> return type
    }.onFailure { throwable ->
        AppLogger.logFirebaseError("Failed to refresh token: ${throwable.message}", throwable)
    }
}
