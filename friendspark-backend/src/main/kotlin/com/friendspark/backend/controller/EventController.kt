package com.friendspark.backend.controller

import com.friendspark.backend.dto.event.EventCreateDTO
import com.friendspark.backend.dto.event.EventCreateResponseDTO
import com.friendspark.backend.dto.event.EventDetailsDTO
import com.friendspark.backend.dto.event.UpdateEventRequest
import com.friendspark.backend.service.AuthorizationService
import com.friendspark.backend.service.EventService
import com.friendspark.backend.service.UserService
import jakarta.validation.Valid
import mu.KotlinLogging
import org.springframework.dao.OptimisticLockingFailureException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/events")
class EventController(
    private val eventService: EventService,
    private val authorizationService: AuthorizationService,
    private val userService: UserService
) {
    private val logger = KotlinLogging.logger {}
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    fun getAllEvents(
        @AuthenticationPrincipal uid: String
    ): ResponseEntity<List<EventDetailsDTO>> {
        logger.info { "Getting all events for user: $uid" }
        try {
            val events = eventService.getAllEvents(uid)
            logger.debug { "Retrieved ${events.size} events for user: $uid" }
            return ResponseEntity.ok(events.map(EventDetailsDTO::fromEntity))
        } catch (e: Exception) {
            logger.error(e) { "Error retrieving events for user: $uid" }
            throw e
        }
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    fun getEventsByUserId(
        @PathVariable userId: UUID,
        @AuthenticationPrincipal uid: String
    ): ResponseEntity<List<EventDetailsDTO>> {
        logger.info { "Getting events for user id: $userId requested by: $uid" }
        try {
            // Authorization: Users can only see their own events unless they are moderators/admins
            val targetUser = userService.getUserEntityById(userId)
            if (targetUser == null) {
                logger.warn { "User not found: $userId requested by: $uid" }
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build()
            }
            
            val isOwnEvents = targetUser.firebaseUid == uid
            val isModeratorOrAdmin = authorizationService.isModeratorOrAdmin(uid)
            
            if (!isOwnEvents && !isModeratorOrAdmin) {
                logger.warn { "Access denied: User $uid attempted to view events for user: $userId" }
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build()
            }
            
            val events = eventService.getEventsByUserId(userId, uid)
            logger.debug { "Retrieved ${events.size} events for user: $userId requested by: $uid" }
            return ResponseEntity.ok(events.map(EventDetailsDTO::fromEntity))
        } catch (e: Exception) {
            logger.error(e) { "Error retrieving events for user: $userId requested by: $uid" }
            throw e
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    fun getEventById(
        @PathVariable id: UUID,
        @AuthenticationPrincipal uid: String
    ): ResponseEntity<EventDetailsDTO> {
        logger.info { "Getting event by id: $id for user: $uid" }
        val event = eventService.getEventById(id)
        
        if (event == null) {
            logger.warn { "Event not found: $id requested by user: $uid" }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
        
        return try {
            authorizationService.verifyCanViewEvent(uid, event)
            logger.debug { "Event retrieved successfully: $id for user: $uid" }
            ResponseEntity.ok(EventDetailsDTO.fromEntity(event))
        } catch (_: AccessDeniedException) {
            logger.warn { "Access denied for event: $id by user: $uid" }
            ResponseEntity.status(HttpStatus.FORBIDDEN).build()
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    fun createEvent(
        @AuthenticationPrincipal uid: String,
        @Valid @RequestBody eventCreate: EventCreateDTO
    ): ResponseEntity<EventCreateResponseDTO> {
        logger.info { "Creating event '${eventCreate.title}' for user: $uid" }
        try {
            val createdEvent = eventService.createEvent(uid, eventCreate)
            logger.info { "Event created successfully: ${createdEvent.id} by user: $uid" }
            return ResponseEntity.status(HttpStatus.CREATED).body(EventCreateResponseDTO(createdEvent.id))
        } catch (e: Exception) {
            logger.error(e) { "Error creating event '${eventCreate.title}' for user: $uid" }
            throw e
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    fun deleteEvent(
        @PathVariable id: UUID,
        @AuthenticationPrincipal uid: String
    ): ResponseEntity<Unit> {
        logger.info { "Deleting event: $id by user: $uid" }
        val event = eventService.getEventById(id)
        
        if (event == null) {
            logger.warn { "Event not found for deletion: $id requested by user: $uid" }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
        
        return try {
            authorizationService.verifyCanDeleteEvent(uid, event)
            eventService.deleteEvent(id)
            logger.info { "Event deleted successfully: $id by user: $uid" }
            ResponseEntity.noContent().build()
        } catch (_: AccessDeniedException) {
            logger.warn { "Access denied for deleting event: $id by user: $uid" }
            ResponseEntity.status(HttpStatus.FORBIDDEN).build()
        }
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    fun patchEvent(
        @PathVariable id: UUID,
        @AuthenticationPrincipal uid: String,
        @Valid @RequestBody patch: UpdateEventRequest
    ): ResponseEntity<EventDetailsDTO> {
        logger.info { "Updating event: $id by user: $uid" }
        val existing = eventService.getEventById(id)
        
        if (existing == null) {
            logger.warn { "Event not found for update: $id requested by user: $uid" }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
        
        return try {
            authorizationService.verifyCanModifyEvent(uid, existing)
            val updated = eventService.updateEvent(existing, patch)
            logger.info { "Event updated successfully: $id by user: $uid" }
            ResponseEntity.ok(EventDetailsDTO.fromEntity(updated))
        } catch (_: AccessDeniedException) {
            logger.warn { "Access denied for updating event: $id by user: $uid" }
            ResponseEntity.status(HttpStatus.FORBIDDEN).build()
        } catch (_: OptimisticLockingFailureException) {
            logger.warn { "Optimistic locking failure for event: $id by user: $uid" }
            ResponseEntity.status(HttpStatus.CONFLICT).build()
        }
    }
}
