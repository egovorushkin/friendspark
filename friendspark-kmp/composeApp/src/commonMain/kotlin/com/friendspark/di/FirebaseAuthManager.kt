package com.friendspark.di

// Expect declaration for multiplatform FirebaseAuthManager
expect class FirebaseAuthManager() {
    suspend fun createUser(email: String, password: String): Result<Unit>
    suspend fun signIn(email: String, password: String): Result<Unit>
    suspend fun getIdToken(): String?
}
