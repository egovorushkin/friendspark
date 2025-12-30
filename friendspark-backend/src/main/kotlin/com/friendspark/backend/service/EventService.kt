package com.friendspark.backend.service

import com.friendspark.backend.dto.event.EventCreateDTO
import com.friendspark.backend.dto.event.EventCreateResponseDTO
import com.friendspark.backend.dto.event.UpdateEventRequest
import com.friendspark.backend.entity.Event
import com.friendspark.backend.entity.User
import com.friendspark.backend.exception.UserNotFoundException
import com.friendspark.backend.mapper.EventMapper
import com.friendspark.backend.repository.EventRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.*

@Service
class EventService(
    private val eventRepository: EventRepository,
    private val userService: UserService,
    private val eventMapper: EventMapper,
    private val authorizationService: AuthorizationService
) {
    private val log = LoggerFactory.getLogger(javaClass)
    /**
     * Gets all events visible to the current user.
     * Hidden events are only visible to their creator, moderators, and admins.
     */
    fun getAllEvents(firebaseUid: String): List<Event> {
        log.debug("Getting all events for user: {}", firebaseUid)
        val allEvents = eventRepository.findAll()
        val isModeratorOrAdmin = authorizationService.isModeratorOrAdmin(firebaseUid)

        val filteredEvents = if (isModeratorOrAdmin) {
            // Moderators and admins can see all events including hidden ones
            log.debug("User {} is moderator/admin, returning all {} events", firebaseUid, allEvents.size)
            allEvents
        } else {
            // Regular users can only see non-hidden events or events they created
            val visibleEvents = allEvents.filter { event ->
                !event.isHidden || event.creator.firebaseUid == firebaseUid
            }
            log.debug("User {} is regular user, returning {} visible events out of {}", firebaseUid, visibleEvents.size, allEvents.size)
            visibleEvents
        }
        return filteredEvents
    }

    fun getEventById(id: UUID): Event? {
        log.debug("Getting event by id: {}", id)
        val event = eventRepository.findById(id).orElse(null)
        if (event == null) {
            log.debug("Event not found: {}", id)
        } else {
            log.debug("Event found: {} (title: {})", id, event.title)
        }
        return event
    }

    fun createEvent(uid: String, eventCreateDTO: EventCreateDTO): EventCreateResponseDTO {
        log.info("Creating event '{}' for user: {}", eventCreateDTO.title, uid)
        val creator: User = userService.findByFirebaseUid(uid)
            ?: run {
                log.error("User not found for Firebase UID: {}", uid)
                throw UserNotFoundException("User with id $uid not found")
            }
        val eventEntity = eventMapper.toEntity(eventCreateDTO, creator)
        val createdEvent = eventRepository.save(eventEntity)
        log.info("Event created successfully: {} (id: {}) by user: {}", eventCreateDTO.title, createdEvent.id, uid)
        return EventCreateResponseDTO(createdEvent.id)
    }

    fun deleteEvent(id: UUID) {
        log.info("Deleting event: {}", id)
        eventRepository.deleteById(id)
        log.debug("Event deleted: {}", id)
    }
    
    /**
     * Updates an event. Authorization check should be performed before calling this method.
     * The authorizationService.verifyCanModifyEvent() should be called in the controller.
     */
    fun updateEvent(existing: Event, patch: UpdateEventRequest): Event {
        log.debug("Updating event: {} (title: {})", existing.id, existing.title)
        // Authorization is verified in the controller before calling this method
        val updateEvent = eventMapper.update(existing, patch)
        val savedEvent = eventRepository.save(updateEvent)
        log.debug("Event updated successfully: {}", savedEvent.id)
        return savedEvent
    }
}
