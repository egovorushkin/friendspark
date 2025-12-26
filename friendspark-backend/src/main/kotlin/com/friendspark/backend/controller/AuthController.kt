package com.friendspark.backend.controller

import com.friendspark.backend.dto.RegisterRequestDTO
import com.friendspark.backend.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/auth")
class AuthController(
    private val userService: UserService
) {

    @PostMapping("/register")
    fun register(
        @RequestBody request: RegisterRequestDTO,
        @RequestHeader("Authorization") authHeader: String
    ): ResponseEntity<Map<String, Any>> {
        val user = userService.registerIfNotExists(
            request,
            authHeader,
        )

        val alreadyRegistered = user.firebaseUid.isNotBlank()

        return ResponseEntity.ok(
            mapOf(
                "success" to true,
                "userId" to (user.id ?: error("User ID not generated")),
                "alreadyRegistered" to alreadyRegistered
            )
        )
    }
}