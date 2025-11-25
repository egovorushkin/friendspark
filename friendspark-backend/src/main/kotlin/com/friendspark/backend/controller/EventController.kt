package com.friendspark.backend.controller

import com.friendspark.backend.dto.event.CreateEventRequest
import com.friendspark.backend.dto.event.EventResponse
import com.friendspark.backend.dto.event.UpdateEventRequest
import com.friendspark.backend.entity.Event
import com.friendspark.backend.service.EventService
import com.friendspark.backend.service.UserService
import jakarta.validation.Valid
import org.springframework.dao.OptimisticLockingFailureException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.Instant
import java.util.UUID

@RestController
@RequestMapping("/api/events")
class EventController(
    private val eventService: EventService,
    private val userService: UserService
) {
    @GetMapping
    fun getAllEvents(): ResponseEntity<List<EventResponse>> =
        ResponseEntity.ok(eventService.getAllEvents().map(EventResponse::fromEntity))

    @GetMapping("/{id}")
    fun getEventById(@PathVariable id: UUID): ResponseEntity<EventResponse> =
        eventService.getEventById(id)
            ?.let { ResponseEntity.ok(EventResponse.fromEntity(it)) }
            ?: ResponseEntity.status(HttpStatus.NOT_FOUND).build()

    @PostMapping
    fun createEvent(@Valid @RequestBody req: CreateEventRequest): ResponseEntity<EventResponse> {
        val creator = userService.getUserById(req.creatorId)
            ?: return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null)
        val eventDate = try {
            Instant.parse(req.eventDate)
        } catch (_: Exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null)
        }
        val event = Event(
            title = req.title,
            locationGeohash = req.locationGeohash,
            description = req.description,
            eventDate = eventDate,
            maxAttendees = req.maxAttendees,
            isPublic = req.isPublic ?: true,
            creator = creator
        )
        val saved = eventService.createEvent(event)
        return ResponseEntity.status(HttpStatus.CREATED).body(EventResponse.fromEntity(saved))
    }

    @DeleteMapping("/{id}")
    fun deleteEvent(@PathVariable id: UUID): ResponseEntity<Void> {
        eventService.deleteEvent(id)
        return ResponseEntity.noContent().build()
    }

    @PatchMapping("/{id}")
    fun patchEvent(
        @PathVariable id: UUID,
        @Valid @RequestBody patch: UpdateEventRequest
    ): ResponseEntity<EventResponse> {
        val existing = eventService.getEventById(id) ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        return try {
            val updated = eventService.updateEvent(existing, patch)
            ResponseEntity.ok(EventResponse.fromEntity(updated))
        } catch (e: OptimisticLockingFailureException) {
            ResponseEntity.status(HttpStatus.CONFLICT).build()
        }
    }
}
