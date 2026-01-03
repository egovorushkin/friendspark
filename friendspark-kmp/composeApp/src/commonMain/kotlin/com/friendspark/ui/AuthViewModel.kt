package com.friendspark.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.friendspark.data.repository.AuthRepository
import com.friendspark.util.AppLogger
import com.friendspark.util.AuthErrorParser
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
        AppLogger.logUI("AuthViewModel: Received event ${event::class.simpleName}")
        when (event) {
            is AuthEvent.Register -> register(event)
            is AuthEvent.Login -> login(event)
        }
    }

    /**
     * Handles user registration event.
     */
    private fun register(event: AuthEvent.Register) {
        AppLogger.logAuth("AuthViewModel: Starting registration process")

        // Clear previous errors
        state = state.copy(
            emailError = null,
            passwordError = null,
            error = null
        )

        // Client-side validation
        val emailError = AuthErrorParser.validateEmailFormat(event.email)
        val passwordError = AuthErrorParser.validatePasswordStrength(event.password)

        if (emailError != null || passwordError != null) {
            state = state.copy(
                emailError = emailError,
                passwordError = passwordError,
                isLoading = false
            )
            AppLogger.logAuthError("AuthViewModel: Client-side validation failed - email: $emailError, password: $passwordError")
            return
        }

        screenModelScope.launch {
            state = state.copy(isLoading = true)
            AppLogger.logUI("AuthViewModel: Registration loading state set to true")

            val result = authRepository.register(
                email = event.email,
                password = event.password
            )

            val error = result.exceptionOrNull()?.message
            val isSuccess = result.isSuccess

            // Parse Firebase errors into field-specific errors
            val (parsedEmailError, parsedPasswordError) = if (error != null) {
                AuthErrorParser.parseRegistrationError(error)
            } else {
                null to null
            }

            state = state.copy(
                isLoading = false,
                error = if (parsedEmailError == null && parsedPasswordError == null) error else null,
                emailError = parsedEmailError,
                passwordError = parsedPasswordError,
                isSuccess = isSuccess
            )

            if (isSuccess) {
                AppLogger.logAuth("AuthViewModel: Registration successful")
            } else {
                AppLogger.logAuthError("AuthViewModel: Registration failed - $error")
            }
        }
    }

    /**
     * Handles user login event.
     */
    private fun login(event: AuthEvent.Login) {
        AppLogger.logAuth("AuthViewModel: Starting login process")

        // Clear previous errors
        state = state.copy(
            emailError = null,
            passwordError = null,
            error = null
        )

        // Client-side validation
        val emailError = AuthErrorParser.validateEmailFormat(event.email)
        val passwordError = AuthErrorParser.validatePasswordForLogin(event.password)

        if (emailError != null || passwordError != null) {
            state = state.copy(
                emailError = emailError,
                passwordError = passwordError,
                isLoading = false
            )
            AppLogger.logAuthError("AuthViewModel: Client-side validation failed - email: $emailError, password: $passwordError")
            return
        }

        screenModelScope.launch {
            state = state.copy(isLoading = true)
            AppLogger.logUI("AuthViewModel: Login loading state set to true")

            val result = authRepository.login(
                email = event.email,
                password = event.password
            )

            val error = result.exceptionOrNull()?.message
            val isSuccess = result.isSuccess

            // Parse Firebase errors into field-specific errors
            val (parsedEmailError, parsedPasswordError) = if (error != null) {
                AuthErrorParser.parseLoginError(error)
            } else {
                null to null
            }

            state = state.copy(
                isLoading = false,
                error = if (parsedEmailError == null && parsedPasswordError == null) error else null,
                emailError = parsedEmailError,
                passwordError = parsedPasswordError,
                isSuccess = isSuccess
            )

            if (isSuccess) {
                AppLogger.logAuth("AuthViewModel: Login successful")
            } else {
                AppLogger.logAuthError("AuthViewModel: Login failed - $error")
            }
        }
    }

    /**
     * Clears email error message
     */
    fun clearEmailError() {
        state = state.copy(emailError = null)
    }

    /**
     * Clears password error message
     */
    fun clearPasswordError() {
        state = state.copy(passwordError = null)
    }
}

/**
 * UI state for authentication screens.
 */
data class AuthState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
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
        val password: String
    ) : AuthEvent()

    /**
     * Event for user login.
     */
    data class Login(val email: String, val password: String) : AuthEvent()
}