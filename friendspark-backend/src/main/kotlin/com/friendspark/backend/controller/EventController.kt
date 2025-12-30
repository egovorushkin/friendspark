package com.friendspark.backend.controller

import com.friendspark.backend.dto.event.EventCreateDTO
import com.friendspark.backend.dto.event.EventCreateResponseDTO
import com.friendspark.backend.dto.event.EventDetailsDTO
import com.friendspark.backend.dto.event.UpdateEventRequest
import com.friendspark.backend.service.AuthorizationService
import com.friendspark.backend.service.EventService
import jakarta.validation.Valid
import org.springframework.dao.OptimisticLockingFailureException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/events")
class EventController(
    private val eventService: EventService,
    private val authorizationService: AuthorizationService
) {
    @GetMapping
    fun getAllEvents(
        @AuthenticationPrincipal uid: String
    ): ResponseEntity<List<EventDetailsDTO>> {
        val events = eventService.getAllEvents(uid)
        return ResponseEntity.ok(events.map(EventDetailsDTO::fromEntity))
    }

    @GetMapping("/{id}")
    fun getEventById(
        @PathVariable id: UUID,
        @AuthenticationPrincipal uid: String
    ): ResponseEntity<EventDetailsDTO> {
        val event = eventService.getEventById(id) ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        
        return try {
            authorizationService.verifyCanViewEvent(uid, event)
            ResponseEntity.ok(EventDetailsDTO.fromEntity(event))
        } catch (e: AccessDeniedException) {
            ResponseEntity.status(HttpStatus.FORBIDDEN).build()
        }
    }

    @PostMapping
    fun createEvent(
        @AuthenticationPrincipal uid: String,
        @Valid @RequestBody eventCreate: EventCreateDTO
    ): ResponseEntity<EventCreateResponseDTO> {
        val createdEvent = eventService.createEvent(uid, eventCreate)
        return ResponseEntity.status(HttpStatus.CREATED).body(EventCreateResponseDTO(createdEvent.id))
    }

    @DeleteMapping("/{id}")
    fun deleteEvent(
        @PathVariable id: UUID,
        @AuthenticationPrincipal uid: String
    ): ResponseEntity<Unit> {
        val event = eventService.getEventById(id) ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        
        return try {
            authorizationService.verifyCanDeleteEvent(uid, event)
            eventService.deleteEvent(id)
            ResponseEntity.noContent().build()
        } catch (e: AccessDeniedException) {
            ResponseEntity.status(HttpStatus.FORBIDDEN).build()
        }
    }

    @PatchMapping("/{id}")
    fun patchEvent(
        @PathVariable id: UUID,
        @AuthenticationPrincipal uid: String,
        @Valid @RequestBody patch: UpdateEventRequest
    ): ResponseEntity<EventDetailsDTO> {
        val existing = eventService.getEventById(id) ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        
        return try {
            authorizationService.verifyCanModifyEvent(uid, existing)
            val updated = eventService.updateEvent(existing, patch, uid)
            ResponseEntity.ok(EventDetailsDTO.fromEntity(updated))
        } catch (e: AccessDeniedException) {
            ResponseEntity.status(HttpStatus.FORBIDDEN).build()
        } catch (_: OptimisticLockingFailureException) {
            ResponseEntity.status(HttpStatus.CONFLICT).build<EventDetailsDTO>()
        }
    }
}
