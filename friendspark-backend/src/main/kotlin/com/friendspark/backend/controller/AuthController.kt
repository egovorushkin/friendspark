package com.friendspark.backend.controller

import com.friendspark.backend.dto.RegisterRequestDTO
import com.friendspark.backend.service.UserService
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/auth")
class AuthController(
    private val userService: UserService
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @PostMapping("/register")
    fun register(
        @RequestBody request: RegisterRequestDTO,
        @RequestHeader("Authorization") authHeader: String
    ): ResponseEntity<Map<String, Any>> {
        log.info("Registration attempt for user: {}", request.name)
        try {
            val user = userService.registerIfNotExists(
                request,
                authHeader,
            )

            val alreadyRegistered = user.firebaseUid.isNotBlank()
            val userId = user.id ?: error("User ID not generated")

            if (alreadyRegistered) {
                log.info("User already registered: {} (id: {})", request.name, userId)
            } else {
                log.info("New user registered successfully: {} (id: {})", request.name, userId)
            }

            return ResponseEntity.ok(
                mapOf(
                    "success" to true,
                    "userId" to userId,
                    "alreadyRegistered" to alreadyRegistered
                )
            )
        } catch (e: Exception) {
            log.error("Registration failed for user: {}", request.name, e)
            throw e
        }
    }
}