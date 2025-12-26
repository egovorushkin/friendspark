package com.friendspark.di

// TODO: Implement actual FirebaseAuthManager for iOS using FirebaseAuth CocoaPod
actual class FirebaseAuthManager actual constructor() {
    actual suspend fun createUser(email: String, password: String): Result<Unit> =
        Result.failure(NotImplementedError("iOS FirebaseAuthManager not implemented yet"))

    actual suspend fun signIn(email: String, password: String): Result<Unit> =
        Result.failure(NotImplementedError("iOS FirebaseAuthManager not implemented yet"))

    actual suspend fun getIdToken(): String? = null
}
