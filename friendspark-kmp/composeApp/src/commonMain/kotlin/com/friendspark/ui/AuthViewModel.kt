package com.friendspark.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.friendspark.data.repository.AuthRepository
import com.friendspark.ui.screens.Location
import kotlinx.coroutines.launch

/**
 * ViewModel for authentication UI logic.
 * Handles registration and login events, manages AuthState.
 * @param authRepository AuthRepository for authentication operations
 */
class AuthViewModel(
    private val authRepository: AuthRepository
) : ScreenModel {
    /**
     * Current authentication state for the UI.
     */
    var state by mutableStateOf(AuthState())
        private set

    /**
     * Handles authentication events (Register, Login).
     */
    fun onEvent(event: AuthEvent) {
        when (event) {
            is AuthEvent.Register -> register(event)
            is AuthEvent.Login -> login(event)
        }
    }

    /**
     * Handles user registration event.
     */
    private fun register(event: AuthEvent.Register) {
        screenModelScope.launch {
            state = state.copy(isLoading = true)
            val result = authRepository.register(
                email = event.email,
                password = event.password,
                name = event.name,
                interests = event.interests,
                location = event.location
            )
            state = state.copy(
                isLoading = false,
                error = result.exceptionOrNull()?.message,
                isSuccess = result.isSuccess
            )
        }
    }

    /**
     * Handles user login event.
     */
    private fun login(event: AuthEvent.Login) {
        screenModelScope.launch {
            state = state.copy(isLoading = true)
            val result = authRepository.login(
                email = event.email,
                password = event.password
            )
            state = state.copy(
                isLoading = false,
                error = result.exceptionOrNull()?.message,
                isSuccess = result.isSuccess
            )
        }
    }
}

/**
 * UI state for authentication screens.
 */
data class AuthState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
)

/**
 * Sealed class for authentication events.
 */
sealed class AuthEvent {
    /**
     * Event for user registration.
     */
    data class Register(
        val email: String,
        val password: String,
        val name: String,
        val interests: List<String>,
        val location: Location
    ) : AuthEvent()
    /**
     * Event for user login.
     */
    data class Login(val email: String, val password: String) : AuthEvent()
}