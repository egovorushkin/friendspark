package com.friendspark.di

actual class FirebaseAuthManager actual constructor() {
    actual suspend fun createUser(email: String, password: String): Result<Unit> =
        Result.failure(NotImplementedError("JVM FirebaseAuthManager not implemented"))

    actual suspend fun signIn(email: String, password: String): Result<Unit> =
        Result.failure(NotImplementedError("JVM FirebaseAuthManager not implemented"))

    actual suspend fun getIdToken(): String? = null
}
