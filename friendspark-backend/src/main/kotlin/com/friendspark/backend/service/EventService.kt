package com.friendspark.backend.service

import com.friendspark.backend.dto.event.EventCreateDTO
import com.friendspark.backend.dto.event.EventCreateResponseDTO
import com.friendspark.backend.dto.event.UpdateEventRequest
import com.friendspark.backend.entity.Event
import com.friendspark.backend.entity.User
import com.friendspark.backend.exception.UserNotFoundException
import com.friendspark.backend.mapper.EventMapper
import com.friendspark.backend.repository.EventRepository
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*

@Service
class EventService(
    private val eventRepository: EventRepository,
    private val userService: UserService,
    private val eventMapper: EventMapper,
    private val authorizationService: AuthorizationService
) {
    /**
     * Gets all events visible to the current user.
     * Hidden events are only visible to their creator, moderators, and admins.
     */
    fun getAllEvents(firebaseUid: String): List<Event> {
        val allEvents = eventRepository.findAll()
        val isModeratorOrAdmin = authorizationService.isModeratorOrAdmin(firebaseUid)
        
        return if (isModeratorOrAdmin) {
            // Moderators and admins can see all events including hidden ones
            allEvents
        } else {
            // Regular users can only see non-hidden events or events they created
            allEvents.filter { event ->
                !event.isHidden || event.creator.firebaseUid == firebaseUid
            }
        }
    }

    fun getEventById(id: UUID): Event? {
        return eventRepository.findById(id).orElse(null)
    }

    fun createEvent(uid: String, eventCreateDTO: EventCreateDTO): EventCreateResponseDTO {
        val creator: User = userService.findByFirebaseUid(uid)
            ?: throw UserNotFoundException("User with id $uid not found")
        val eventEntity = eventMapper.toEntity(eventCreateDTO, creator)
        val createdEvent = eventRepository.save(eventEntity)
        return EventCreateResponseDTO(createdEvent.id)
    }

    fun deleteEvent(id: UUID) = eventRepository.deleteById(id)
    
    /**
     * Updates an event. Authorization check should be performed before calling this method.
     * The authorizationService.verifyCanModifyEvent() should be called in the controller.
     */
    fun updateEvent(existing: Event, patch: UpdateEventRequest, firebaseUid: String): Event {
        // Authorization is verified in the controller before calling this method
        patch.title?.let { existing.title = it }
        patch.geohash?.let { existing.geohash = it }
        patch.latitude?.let { existing.latitude = it }
        patch.longitude?.let { existing.longitude = it }
        patch.description?.let { existing.description = it }
        patch.eventDate?.let { existing.eventDate = Instant.parse(it) }
        patch.maxAttendees?.let { existing.maxAttendees = it }
        patch.isPublic?.let { existing.isPublic = it }
        return eventRepository.save(existing)
    }
}
