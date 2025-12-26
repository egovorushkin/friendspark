package com.friendspark.data.repository

import com.friendspark.di.FriendSparkApi
import com.friendspark.di.FirebaseAuthManager
import com.friendspark.ui.screens.Location
import kotlin.getOrThrow

/**
 * Repository for authentication and registration logic.
 * Handles user registration via Firebase and backend, and login via Firebase.
 * @param firebaseAuth Platform-specific FirebaseAuthManager
 * @param api FriendSparkApi for backend communication
 */
class AuthRepository(
    private val firebaseAuth: FirebaseAuthManager,
    private val api: FriendSparkApi
) {
    /**
     * Registers a new user using Firebase and backend API.
     * @return Result<Unit> indicating success or failure
     */
    suspend fun register(
        email: String,
        password: String,
        name: String,
        interests: List<String>,
        location: Location
    ): Result<Unit> = runCatching {
        // 1. Firebase
        firebaseAuth.createUser(email, password).getOrThrow<Unit>()

        // 2. Get token
        val token = firebaseAuth.getIdToken() ?: throw Exception("No token")

        // 3. Register on backend
        api.register(
            RegisterRequest(
                name = name,
                interests = interests,
                latitude = location.latitude,
                longitude = location.longitude,
                token = token
            )
        ).takeIf { it.success } ?: throw Exception("Backend registration failed")
    }

    /**
     * Logs in a user using Firebase.
     * @return Result<Unit> indicating success or failure
     */
    suspend fun login(email: String, password: String): Result<Unit> =
        firebaseAuth.signIn(email, password).map { }
}

/**
 * Data class for registration request payload.
 */
data class RegisterRequest(
    val name: String,
    val interests: List<String>,
    val latitude: Double,
    val longitude: Double,
    val token: String? = null
)

/**
 * Data class for registration response payload.
 */
data class RegisterResponse(val success: Boolean, val userId: String?)
