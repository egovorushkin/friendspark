package com.friendspark.util

/**
 * Utility for parsing Firebase authentication errors and converting them
 * to user-friendly validation messages.
 */
object AuthErrorParser {

    /**
     * Parses Firebase error message and returns user-friendly error message
     * @param errorMessage The error message from Firebase exception (may include error code prefix)
     * @return Pair of (emailError, passwordError) - one will be null, the other will have the error message
     */
    fun parseRegistrationError(errorMessage: String?): Pair<String?, String?> {
        if (errorMessage == null) return null to null

        val lowerMessage = errorMessage.lowercase()

        return when {
            // Email already exists - check for error code first
            lowerMessage.startsWith("error_email_already_in_use:") ||
                    lowerMessage.contains("error_email_already_in_use") ||
                    lowerMessage.contains("email-already-in-use") ||
                    lowerMessage.contains("email already exists") ||
                    lowerMessage.contains("email address is already in use") -> {
                "This email address is already registered. Please use a different email or try logging in." to null
            }

            // Invalid email format
            lowerMessage.startsWith("error_invalid_email:") ||
                    lowerMessage.contains("error_invalid_email") ||
                    lowerMessage.contains("invalid-email") ||
                    lowerMessage.contains("invalid email") ||
                    lowerMessage.contains("malformed email") -> {
                "Please enter a valid email address." to null
            }

            // Weak password
            lowerMessage.startsWith("error_weak_password:") ||
                    lowerMessage.contains("error_weak_password") ||
                    lowerMessage.contains("weak-password") ||
                    lowerMessage.contains("password should be at least") ||
                    lowerMessage.contains("password is too weak") -> {
                null to "Password is too weak. Please use at least 6 characters."
            }

            // Password too short
            lowerMessage.contains("password must be at least") ||
                    lowerMessage.contains("password too short") ||
                    lowerMessage.contains("password should be at least 6") -> {
                null to "Password must be at least 6 characters long."
            }

            // Invalid password
            lowerMessage.startsWith("error_invalid_password:") ||
                    lowerMessage.contains("error_invalid_password") ||
                    lowerMessage.contains("invalid-password") ||
                    lowerMessage.contains("invalid password") -> {
                null to "Invalid password. Please check your password and try again."
            }

            // Network errors
            lowerMessage.contains("network") ||
                    lowerMessage.contains("connection") ||
                    lowerMessage.contains("timeout") ||
                    lowerMessage.contains("network_error") -> {
                "Network error. Please check your internet connection and try again." to null
            }

            // Default error - extract message after colon if present
            else -> {
                val message = if (errorMessage.contains(":")) {
                    errorMessage.substringAfter(":").trim()
                } else {
                    errorMessage
                }
                message to null
            }
        }
    }

    /**
     * Validates email format on client side
     */
    fun validateEmailFormat(email: String): String? {
        if (email.isBlank()) {
            return "Email is required"
        }

        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$"
        if (!email.matches(emailRegex.toRegex())) {
            return "Please enter a valid email address"
        }

        return null
    }

    /**
     * Validates password strength on client side
     */
    fun validatePasswordStrength(password: String): String? {
        if (password.isBlank()) {
            return "Password is required"
        }

        if (password.length < 6) {
            return "Password must be at least 6 characters long"
        }

        // Optional: Add more password strength checks
        // if (password.all { it.isLetter() }) {
        //     return "Password should contain at least one number or special character"
        // }

        return null
    }

    /**
     * Validates password for login (just checks if not blank)
     */
    fun validatePasswordForLogin(password: String): String? {
        if (password.isBlank()) {
            return "Password is required"
        }
        return null
    }

    /**
     * Parses Firebase login error message and returns user-friendly error message
     * @param errorMessage The error message from Firebase exception
     * @return Pair of (emailError, passwordError) - one will be null, the other will have the error message
     */
    fun parseLoginError(errorMessage: String?): Pair<String?, String?> {
        if (errorMessage == null) return null to null

        val lowerMessage = errorMessage.lowercase()

        return when {
            // User not found / Invalid email
            lowerMessage.startsWith("error_user_not_found:") ||
                    lowerMessage.contains("error_user_not_found") ||
                    lowerMessage.contains("user-not-found") ||
                    lowerMessage.contains("there is no user record") -> {
                "No account found with this email address. Please check your email or sign up." to null
            }

            // Wrong password
            lowerMessage.startsWith("error_wrong_password:") ||
                    lowerMessage.contains("error_wrong_password") ||
                    lowerMessage.contains("wrong-password") ||
                    lowerMessage.contains("the password is invalid") ||
                    lowerMessage.contains("password is incorrect") -> {
                null to "Incorrect password. Please try again."
            }

            // Invalid email format
            lowerMessage.startsWith("error_invalid_email:") ||
                    lowerMessage.contains("error_invalid_email") ||
                    lowerMessage.contains("invalid-email") ||
                    lowerMessage.contains("invalid email") ||
                    lowerMessage.contains("malformed email") -> {
                "Please enter a valid email address." to null
            }

            // Too many attempts
            lowerMessage.contains("too-many-requests") ||
                    lowerMessage.contains("too many requests") ||
                    lowerMessage.contains("too many attempts") -> {
                "Too many login attempts. Please try again later." to null
            }

            // Network errors
            lowerMessage.contains("network") ||
                    lowerMessage.contains("connection") ||
                    lowerMessage.contains("timeout") ||
                    lowerMessage.contains("network_error") -> {
                "Network error. Please check your internet connection and try again." to null
            }

            // Default error - extract message after colon if present
            else -> {
                val message = if (errorMessage.contains(":")) {
                    errorMessage.substringAfter(":").trim()
                } else {
                    errorMessage
                }
                message to null
            }
        }
    }
}

