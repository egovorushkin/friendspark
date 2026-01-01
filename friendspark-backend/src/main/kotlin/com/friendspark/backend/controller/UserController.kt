package com.friendspark.backend.controller

import com.friendspark.backend.dto.user.PromoteToAdminRequest
import com.friendspark.backend.dto.user.UserDetailsDTO
import com.friendspark.backend.dto.user.UserUpdateDTO
import com.friendspark.backend.service.AuthorizationService
import com.friendspark.backend.service.UserService
import jakarta.validation.Valid
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/users")
class UserController(
    private val userService: UserService,
    private val authorizationService: AuthorizationService
) {
    private val logger = KotlinLogging.logger {}

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    fun getAllUsers(
        @AuthenticationPrincipal uid: String
    ): ResponseEntity<List<UserDetailsDTO>> {
        logger.info { "Getting all users requested by: $uid" }
        val users = userService.getAllUsers()
        logger.debug { "Retrieved ${users.size} users for admin/moderator: $uid" }
        return ResponseEntity.ok(users)
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    fun me(@AuthenticationPrincipal uid: String): ResponseEntity<Map<String, Any?>> {
        logger.debug { "Getting current user profile for: $uid" }
        try {
            val user = authorizationService.getCurrentUser(uid)
            logger.debug { "Retrieved profile for user: $uid (id: ${user.id})" }
            return ResponseEntity.ok(
                mapOf(
                    "id" to user.id,
                    "email" to user.email,
                    "firstName" to user.firstName,
                    "lastName" to user.lastName,
                    "role" to user.role.name
                )
            )
        } catch (e: Exception) {
            logger.error(e) { "Error retrieving profile for user: $uid" }
            throw e
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    fun getUserDetails(
        @PathVariable id: UUID,
        @AuthenticationPrincipal uid: String
    ): ResponseEntity<UserDetailsDTO> {
        logger.info { "Getting user details: $id requested by: $uid" }
        val targetUser = userService.getUserEntityById(id)
        
        if (targetUser == null) {
            logger.warn { "User not found: $id requested by: $uid" }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
        
        return try {
            authorizationService.verifyCanViewUser(uid, targetUser)
            val userDetails = userService.getUserById(id) ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).build()
            logger.debug { "User details retrieved: $id by: $uid" }
            ResponseEntity.ok(userDetails)
        } catch (_: AccessDeniedException) {
            logger.warn { "Access denied: User $uid attempted to view user: $id" }
            ResponseEntity.status(HttpStatus.FORBIDDEN).build()
        }
    }

    @GetMapping("/nearby")
    @PreAuthorize("hasRole('USER')")
    fun findNearbyUsers(
        @RequestParam geohashPrefix: String,
        @AuthenticationPrincipal uid: String
    ): List<UserDetailsDTO> {
        logger.info { "Finding nearby users with geohash prefix: $geohashPrefix for user: $uid" }
        try {
            val users = userService.findNearbyUsers(geohashPrefix)
            logger.debug { "Found ${users.size} nearby users for geohash prefix: $geohashPrefix by user: $uid" }
            return users
        } catch (e: Exception) {
            logger.error(e) { "Error finding nearby users for geohash prefix: $geohashPrefix by user: $uid" }
            throw e
        }
    }

    @PutMapping("/me")
    @PreAuthorize("hasRole('USER')")
    fun updateProfile(
        @AuthenticationPrincipal uid: String,
        @RequestBody updateDTO: UserUpdateDTO
    ): UserDetailsDTO {
        logger.info { "Updating profile for user: $uid" }
        try {
            val updated = userService.updateUserProfile(uid, updateDTO)
            logger.info { "Profile updated successfully for user: $uid" }
            return updated
        } catch (e: Exception) {
            logger.error(e) { "Error updating profile for user: $uid" }
            throw e
        }
    }

    @PostMapping("/admin/promote")
    @PreAuthorize("hasRole('ADMIN')")
    fun promoteToAdmin(
        @AuthenticationPrincipal uid: String,
        @Valid @RequestBody request: PromoteToAdminRequest
    ): ResponseEntity<UserDetailsDTO> {
        logger.info { "Admin promotion request for user: ${request.userId} by: $uid" }
        try {
            val promotedUser = userService.promoteToAdmin(request.userId, uid)
            logger.info { "User ${request.userId} successfully promoted to admin by: $uid" }
            return ResponseEntity.ok(promotedUser)
        } catch (e: IllegalArgumentException) {
            logger.warn { "Failed to promote user ${request.userId} to admin: ${e.message}" }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        } catch (e: Exception) {
            logger.error(e) { "Error promoting user ${request.userId} to admin by: $uid" }
            throw e
        }
    }
}