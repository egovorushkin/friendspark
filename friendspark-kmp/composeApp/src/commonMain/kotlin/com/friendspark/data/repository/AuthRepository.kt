package com.friendspark.data.repository

import com.friendspark.di.FirebaseAuthManager
import com.friendspark.di.FriendSparkApi
import com.friendspark.util.AppLogger
import kotlinx.serialization.Serializable

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
        password: String
    ): Result<Unit> = runCatching {
        AppLogger.logAuth("Starting user registration for email: ${email.take(3)}***")

        // 1. Create user with Firebase
        AppLogger.logFirebase("Creating user with Firebase Auth")
        firebaseAuth.createUser(email, password).getOrThrow().also {
            AppLogger.logFirebase("Firebase user created successfully")
        }

        // 2. Extract ID Token (JWT) from Firebase result
        AppLogger.logAuth("Extracting ID token from Firebase")
        val token = firebaseAuth.getIdToken(forceRefresh = false) ?: run {
            AppLogger.logAuthError("Failed to get ID token from Firebase")
            throw Exception("No token available")
        }
        AppLogger.logAuth("ID token extracted successfully (length: ${token.length})")

        // 3. Call backend POST /api/auth/register with token in header
        AppLogger.logApi("Calling backend registration endpoint")
        val response = api.register(
            RegisterRequest(
                token = token
            )
        )

        if (!response.success) {
            AppLogger.logApiError("Backend registration failed: ${response.userId}")
            throw Exception("Backend registration failed")
        }
        AppLogger.logApi("Backend registration successful, userId: ${response.userId}")

        // 4. Force token refresh after successful registration to get updated claims
        AppLogger.logAuth("Refreshing token to get updated custom claims")
        firebaseAuth.refreshToken().getOrThrow().also {
            AppLogger.logAuth("Token refreshed successfully")
        }

        AppLogger.logAuth("User registration completed successfully")
    }.onFailure { throwable ->
        AppLogger.logAuthError("Registration failed: ${throwable.message}", throwable)
    }

    /**
     * Logs in a user using Firebase.
     * @return Result<Unit> indicating success or failure
     */
    suspend fun login(email: String, password: String): Result<Unit> = runCatching {
        AppLogger.logAuth("Starting user login for email: ${email.take(3)}***")
        firebaseAuth.signIn(email, password).getOrThrow().also {
            AppLogger.logAuth("User login successful")
        }
    }.onFailure { throwable ->
        AppLogger.logAuthError("Login failed: ${throwable.message}", throwable)
    }
}

/**
 * Data class for registration request payload.
 */
@Serializable
data class RegisterRequest(
    val token: String? = null
)

/**
 * Data class for registration response payload.
 * Matches the backend response structure.
 */
@Serializable
data class RegisterResponse(
    val success: Boolean,
    val userId: String? = null,
    val alreadyRegistered: Boolean = false
)
