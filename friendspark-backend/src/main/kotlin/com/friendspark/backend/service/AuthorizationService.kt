package com.friendspark.backend.service

import com.friendspark.backend.entity.Event
import com.friendspark.backend.entity.User
import com.friendspark.backend.entity.UserRole
import org.slf4j.LoggerFactory
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Service

/**
 * Service for handling authorization checks.
 * Centralizes authorization logic to ensure consistent security across the application.
 */
@Service
class AuthorizationService(
    private val userService: UserService
) {
    private val log = LoggerFactory.getLogger(javaClass)

    /**
     * Checks if a user (identified by Firebase UID) is the creator of an event.
     */
    fun isEventCreator(firebaseUid: String, event: Event): Boolean {
        return event.creator.firebaseUid == firebaseUid
    }

    /**
     * Checks if a user has moderator or admin role.
     */
    fun isModeratorOrAdmin(firebaseUid: String): Boolean {
        val user = userService.findByFirebaseUid(firebaseUid) ?: return false
        return user.role == UserRole.MODERATOR || user.role == UserRole.ADMIN
    }

    /**
     * Checks if a user can modify an event.
     * Users can modify events if they are the creator OR if they are a moderator/admin.
     */
    fun canModifyEvent(firebaseUid: String, event: Event): Boolean {
        return isEventCreator(firebaseUid, event) || isModeratorOrAdmin(firebaseUid)
    }

    /**
     * Checks if a user can delete an event.
     * Users can delete events if they are the creator OR if they are a moderator/admin.
     */
    fun canDeleteEvent(firebaseUid: String, event: Event): Boolean {
        return isEventCreator(firebaseUid, event) || isModeratorOrAdmin(firebaseUid)
    }

    /**
     * Checks if a user can view an event.
     * Hidden events can only be viewed by moderators/admins or the creator.
     */
    fun canViewEvent(firebaseUid: String, event: Event): Boolean {
        if (!event.isHidden) {
            return true
        }
        // Hidden events can only be viewed by creator, moderator, or admin
        return isEventCreator(firebaseUid, event) || isModeratorOrAdmin(firebaseUid)
    }

    /**
     * Checks if a user can view another user's profile.
     * Banned users' profiles should be restricted.
     */
    fun canViewUser(firebaseUid: String, targetUser: User): Boolean {
        // If target user is banned, only moderators/admins can view
        if (targetUser.isBanned) {
            return isModeratorOrAdmin(firebaseUid)
        }
        // Users can always view non-banned users
        return true
    }

    /**
     * Verifies that a user can modify an event, throwing AccessDeniedException if not.
     */
    fun verifyCanModifyEvent(firebaseUid: String, event: Event) {
        if (!canModifyEvent(firebaseUid, event)) {
            throw AccessDeniedException("You do not have permission to modify this event")
        }
    }

    /**
     * Verifies that a user can delete an event, throwing AccessDeniedException if not.
     */
    fun verifyCanDeleteEvent(firebaseUid: String, event: Event) {
        if (!canDeleteEvent(firebaseUid, event)) {
            throw AccessDeniedException("You do not have permission to delete this event")
        }
    }

    /**
     * Verifies that a user can view an event, throwing AccessDeniedException if not.
     */
    fun verifyCanViewEvent(firebaseUid: String, event: Event) {
        if (!canViewEvent(firebaseUid, event)) {
            throw AccessDeniedException("You do not have permission to view this event")
        }
    }

    /**
     * Verifies that a user can view another user's profile, throwing AccessDeniedException if not.
     */
    fun verifyCanViewUser(firebaseUid: String, targetUser: User) {
        if (!canViewUser(firebaseUid, targetUser)) {
            throw AccessDeniedException("You do not have permission to view this user")
        }
    }

    /**
     * Gets the current user entity from Firebase UID.
     * Throws exception if user not found.
     */
    fun getCurrentUser(firebaseUid: String): User {
        return userService.findByFirebaseUid(firebaseUid)
            ?: throw IllegalArgumentException("User not found for Firebase UID: $firebaseUid")
    }
}

