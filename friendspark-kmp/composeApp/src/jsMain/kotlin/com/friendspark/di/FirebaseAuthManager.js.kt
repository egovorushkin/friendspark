package com.friendspark.di

actual class FirebaseAuthManager actual constructor() {
    actual suspend fun createUser(
        email: String,
        password: String
    ): Result<Unit> {
        TODO("Not yet implemented")
    }

    actual suspend fun signIn(
        email: String,
        password: String
    ): Result<Unit> {
        TODO("Not yet implemented")
    }

    actual suspend fun getIdToken(forceRefresh: Boolean): String? {
        TODO("Not yet implemented")
    }

    actual suspend fun refreshToken(): Result<Unit> {
        TODO("Not yet implemented")
    }
}