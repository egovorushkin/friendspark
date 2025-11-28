package com.friendspark.backend.controller

import com.friendspark.backend.service.UserService
import com.friendspark.backend.util.Geohash
import com.google.firebase.auth.FirebaseAuth
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/auth")
class AuthController(
    private val userService: UserService
) {

    data class RegisterRequest(
        val name: String,
        val interests: List<String>,
        val latitude: Double,
        val longitude: Double
    )

    @PostMapping("/register")
    fun register(
        @RequestBody request: RegisterRequest,
        @RequestHeader("Authorization") authHeader: String
    ): ResponseEntity<Map<String, Any>> {
        val token = authHeader.removePrefix("Bearer ").trim()
        val decodedToken = FirebaseAuth.getInstance().verifyIdToken(token)

        val geohash = Geohash.encode(request.latitude, request.longitude, 12)

        val user = userService.registerIfNotExists(
            firebaseUid = decodedToken.uid,
            email = decodedToken.email ?: "",
            name = request.name,
            interests = request.interests,
            geohash = geohash,
            latitude = request.latitude,
            longitude = request.longitude,
        )

        val alreadyRegistered = !user.isOnboarded || user.interests.isNotEmpty()

        return ResponseEntity.ok(
            mapOf(
                "success" to true,
                "userId" to (user.id ?: error("User ID not generated")),
                "alreadyRegistered" to alreadyRegistered
            )
        )
    }
}