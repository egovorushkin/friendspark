package com.friendspark.backend.security

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseToken
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.util.concurrent.ExecutionException

/**
 * This is the core security logic. It checks for the Authorization: Bearer <token> header,
 * validates the token using the Firebase Admin SDK, and creates a valid Spring Security authentication object.
 */

@Component
class FirebaseTokenFilter : OncePerRequestFilter() {

    private val log = LoggerFactory.getLogger(javaClass)
    private val BEARER_PREFIX = "Bearer "

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val header = request.getHeader("Authorization")

        // 1. Check if the Authorization header is present and starts with "Bearer "
        if (header.isNullOrEmpty() || !header.startsWith(BEARER_PREFIX)) {
            filterChain.doFilter(request, response)
            return
        }

        val idToken = header.substring(BEARER_PREFIX.length)

        try {
            // 2. Validate the token using Firebase Admin SDK (Step 5 & 6 in diagram)
            val firebaseToken: FirebaseToken = FirebaseAuth.getInstance().verifyIdTokenAsync(idToken).get()

            // 3. The Token is valid. Extract User ID (UID) and create Spring Authentication object
            val uid = firebaseToken.uid

            // You can extract custom claims from firebaseToken.claims to map roles/authorities
            val authorities = listOf(SimpleGrantedAuthority("ROLE_USER"))

            // Create a custom authentication token. The UID is the principal.
            val authentication = UsernamePasswordAuthenticationToken(uid, idToken, authorities)

            // 4. Set the authentication object in Spring's Security Context
            SecurityContextHolder.getContext().authentication = authentication
            log.debug("Successfully authenticated Firebase user with UID: {}", uid)

        } catch (e: ExecutionException) {
            // This catches token validation failures (expired, invalid signature, etc.)
            log.warn("Firebase ID Token validation failed: {}", e.cause?.message)
            // Optionally, clear context if needed, but the original context is preserved if not explicitly set
            SecurityContextHolder.clearContext()
        } catch (e: Exception) {
            // General exceptions
            log.error("Unexpected error during token processing: {}", e.message)
        }

        filterChain.doFilter(request, response)
    }
}