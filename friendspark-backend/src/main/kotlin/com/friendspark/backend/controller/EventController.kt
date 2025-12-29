package com.friendspark.backend.controller

import com.friendspark.backend.dto.event.EventCreateDTO
import com.friendspark.backend.dto.event.EventCreateResponseDTO
import com.friendspark.backend.dto.event.EventDetailsDTO
import com.friendspark.backend.dto.event.UpdateEventRequest
import com.friendspark.backend.service.EventService
import jakarta.validation.Valid
import org.springframework.dao.OptimisticLockingFailureException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/events")
class EventController(
    private val eventService: EventService
) {
    @GetMapping
    fun getAllEvents(): ResponseEntity<List<EventDetailsDTO>> =
        ResponseEntity.ok(eventService.getAllEvents().map(EventDetailsDTO::fromEntity))

    @GetMapping("/{id}")
    fun getEventById(@PathVariable id: UUID): ResponseEntity<EventDetailsDTO> =
        eventService.getEventById(id)
            ?.let { ResponseEntity.ok(EventDetailsDTO.fromEntity(it)) }
            ?: ResponseEntity.status(HttpStatus.NOT_FOUND).build()

    @PostMapping
    fun createEvent(
        @AuthenticationPrincipal uid: String,
        @Valid @RequestBody eventCreate: EventCreateDTO
    ): ResponseEntity<EventCreateResponseDTO> {
        val createdEvent = eventService.createEvent(uid, eventCreate)
        return ResponseEntity.status(HttpStatus.CREATED).body(EventCreateResponseDTO(createdEvent.id))
    }

    @DeleteMapping("/{id}")
    fun deleteEvent(@PathVariable id: UUID): ResponseEntity<Unit> {
        eventService.deleteEvent(id)
        return ResponseEntity.noContent().build()
    }

    @PatchMapping("/{id}")
    fun patchEvent(
        @PathVariable id: UUID,
        @Valid @RequestBody patch: UpdateEventRequest
    ): ResponseEntity<EventDetailsDTO> {
        val existing = eventService.getEventById(id) ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        return try {
            val updated = eventService.updateEvent(existing, patch)
            ResponseEntity.ok(EventDetailsDTO.fromEntity(updated))
        } catch (_: OptimisticLockingFailureException) {
            ResponseEntity.status(HttpStatus.CONFLICT).build<EventDetailsDTO>()
        }
    }
}
