package com.friendspark.di

actual class FirebaseAuthManager actual constructor() {
    actual suspend fun createUser(email: String, password: String): Result<Unit> =
        Result.failure(NotImplementedError("WASM FirebaseAuthManager not implemented"))

    actual suspend fun signIn(email: String, password: String): Result<Unit> =
        Result.failure(NotImplementedError("WASM FirebaseAuthManager not implemented"))

    actual suspend fun getIdToken(forceRefresh: Boolean): String? = null

    actual suspend fun refreshToken(): Result<Unit> =
        Result.failure(NotImplementedError("WASM FirebaseAuthManager not implemented"))
}
