package com.friendspark.backend.controller

import com.friendspark.backend.dto.user.UserDetailsDTO
import com.friendspark.backend.dto.user.UserUpdateDTO
import com.friendspark.backend.service.AuthorizationService
import com.friendspark.backend.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/users")
class UserController(
    private val userService: UserService,
    private val authorizationService: AuthorizationService
) {

    @GetMapping
    fun getAllUsers(
        @AuthenticationPrincipal uid: String
    ): ResponseEntity<List<UserDetailsDTO>> {
        // Only moderators and admins can see all users
        // Regular users should use the /nearby endpoint or search
        if (!authorizationService.isModeratorOrAdmin(uid)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build()
        }
        return ResponseEntity.ok(userService.getAllUsers())
    }

    @GetMapping("/me")
    fun me(@AuthenticationPrincipal uid: String): ResponseEntity<Map<String, Any?>> {
        val user = authorizationService.getCurrentUser(uid)
        return ResponseEntity.ok(
            mapOf(
                "id" to user.id,
                "email" to user.email,
                "firstName" to user.firstName,
                "lastName" to user.lastName,
                "role" to user.role.name
            )
        )
    }

    @GetMapping("/{id}")
    fun getUserDetails(
        @PathVariable id: UUID,
        @AuthenticationPrincipal uid: String
    ): ResponseEntity<UserDetailsDTO> {
        val targetUser = userService.getUserEntityById(id) ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        
        return try {
            authorizationService.verifyCanViewUser(uid, targetUser)
            val userDetails = userService.getUserById(id) ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).build()
            ResponseEntity.ok(userDetails)
        } catch (e: AccessDeniedException) {
            ResponseEntity.status(HttpStatus.FORBIDDEN).build()
        }
    }

    @GetMapping("/nearby")
    fun findNearbyUsers(
        @RequestParam geohashPrefix: String,
        @AuthenticationPrincipal uid: String
    ): List<UserDetailsDTO> {
        // TODO: Filter out banned users at repository level for better performance
        // Currently returns all nearby users - filtering by banned status should be added
        return userService.findNearbyUsers(geohashPrefix)
    }

    @PutMapping("/me")
    fun updateProfile(
        @AuthenticationPrincipal uid: String,
        @RequestBody updateDTO: UserUpdateDTO
    ): UserDetailsDTO = userService.updateUserProfile(uid, updateDTO)
}