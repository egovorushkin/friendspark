package com.friendspark.backend.config

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.util.*

/**
 * This component ensures the Firebase Admin SDK is initialized once when the application starts.
 */
@Configuration
class FirebaseAdminConfig {

    private val log = LoggerFactory.getLogger(javaClass)
    private val ENV_VAR_NAME = "FIREBASE_CREDENTIALS_BASE64"
    private val FILE_NAME = "firebase-service-account.json"

    /**
     * Initializes the Firebase Admin SDK instance.
     * Prioritizes reading credentials from a Base64-encoded environment variable
     * for secure deployment, falling back to a local resource file for dev.
     */
    @PostConstruct
    fun initialize() {
        val credentialStream: InputStream? = getCredentialStream()

        if (credentialStream == null) {
            log.error("Firebase initialization failed. No credentials found via environment variable or local file.")
            // throw RuntimeException("Firebase initialization failed: Missing credentials.")
            return
        }

        try {
            val options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(credentialStream))
                // Optionally set database URL if using Firebase Database/Firestore
                // .setDatabaseUrl("https://<YOUR_FIREBASE_PROJECT_ID>.firebaseio.com")
                .build()

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options)
                log.info("Firebase Admin SDK initialized successfully using provided credentials.")
            } else {
                log.warn("Firebase Admin SDK was already initialized.")
            }
        } catch (e: Exception) {
            log.error("Failed to initialize Firebase Admin SDK: {}", e.message)
            // throw RuntimeException("Firebase initialization failed.", e)
        }
    }

    /**
     * Attempts to get credentials, prioritizing environment variable.
     */
    private fun getCredentialStream(): InputStream? {
        // 1. Check for Base64 encoded environment variable (Production)
        val base64Credentials = System.getenv(ENV_VAR_NAME)
        if (!base64Credentials.isNullOrEmpty()) {
            log.info("Loading Firebase credentials from Base64 environment variable: {}", ENV_VAR_NAME)
            try {
                val decodedJson = Base64.getDecoder().decode(base64Credentials)
                return ByteArrayInputStream(decodedJson)
            } catch (e: IllegalArgumentException) {
                log.error("Failed to decode Base64 credentials from environment variable.", e)
                return null
            }
        }

        // 2. Fallback to local file (Development)
        return try {
            val resource = ClassPathResource(FILE_NAME)
            if (resource.exists()) {
                log.warn(
                    "Loading Firebase credentials from local file: {}. Use environment variable for production!",
                    FILE_NAME
                )
                resource.inputStream
            } else {
                null
            }
        } catch (e: Exception) {
            log.error("Error reading local service account file.", e)
            null
        }
    }
}