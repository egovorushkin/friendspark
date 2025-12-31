package com.friendspark.backend.service

import com.friendspark.backend.dto.event.EventCreateDTO
import com.friendspark.backend.dto.event.EventCreateResponseDTO
import com.friendspark.backend.dto.event.UpdateEventRequest
import com.friendspark.backend.entity.Event
import com.friendspark.backend.entity.User
import com.friendspark.backend.exception.UserNotFoundException
import com.friendspark.backend.mapper.EventMapper
import com.friendspark.backend.repository.EventRepository
import mu.KotlinLogging
import org.springframework.stereotype.Service
import java.util.*

@Service
class EventService(
    private val eventRepository: EventRepository,
    private val userService: UserService,
    private val eventMapper: EventMapper,
    private val authorizationService: AuthorizationService
) {
    private val logger = KotlinLogging.logger {}
    /**
     * Gets all events visible to the current user.
     * Hidden events are only visible to their creator, moderators, and admins.
     */
    fun getAllEvents(firebaseUid: String): List<Event> {
        logger.debug { "Getting all events for user: $firebaseUid" }
        val allEvents = eventRepository.findAllWithCreator()
        val isModeratorOrAdmin = authorizationService.isModeratorOrAdmin(firebaseUid)

        val filteredEvents = if (isModeratorOrAdmin) {
            // Moderators and admins can see all events including hidden ones
            logger.debug { "User $firebaseUid is moderator/admin, returning all \\${allEvents.size} events" }
            allEvents
        } else {
            // Regular users can only see non-hidden events or events they created
            val visibleEvents = allEvents.filter { event ->
                !event.isHidden && event.creator.firebaseUid == firebaseUid
            }
            logger.debug { "User $firebaseUid is regular user, returning \\${visibleEvents.size} visible events out of \\${allEvents.size}" }
            visibleEvents
        }
        return filteredEvents
    }

    fun getEventById(id: UUID): Event? {
        logger.debug { "Getting event by id: $id" }
        val event = eventRepository.findById(id).orElse(null)
        if (event == null) {
            logger.debug { "Event not found: $id" }
        } else {
            logger.debug { "Event found: $id (title: ${event.title})" }
        }
        return event
    }

    fun createEvent(uid: String, eventCreateDTO: EventCreateDTO): EventCreateResponseDTO {
        logger.info { "Creating event '${eventCreateDTO.title}' for user: $uid" }
        val creator: User = userService.findByFirebaseUid(uid)
            ?: run {
                logger.error { "User not found for Firebase UID: $uid" }
                throw UserNotFoundException("User with id $uid not found")
            }
        val eventEntity = eventMapper.toEntity(eventCreateDTO, creator)
        val createdEvent = eventRepository.save(eventEntity)
        logger.info { "Event created successfully: ${eventCreateDTO.title} (id: ${createdEvent.id}) by user: $uid" }
        return EventCreateResponseDTO(createdEvent.id)
    }

    fun deleteEvent(id: UUID) {
        logger.info { "Deleting event: $id" }
        eventRepository.deleteById(id)
        logger.debug { "Event deleted: $id" }
    }
    
    /**
     * Updates an event. Authorization check should be performed before calling this method.
     * The authorizationService.verifyCanModifyEvent() should be called in the controller.
     */
    fun updateEvent(existing: Event, patch: UpdateEventRequest): Event {
        logger.debug { "Updating event: ${existing.id} (title: ${existing.title})" }
        // Authorization is verified in the controller before calling this method
        val updateEvent = eventMapper.update(existing, patch)
        val savedEvent = eventRepository.save(updateEvent)
        logger.debug { "Event updated successfully: ${savedEvent.id}" }
        return savedEvent
    }

    /**
     * Gets all events created by a specific user.
     * Authorization should be checked in the controller - users can only see their own events
     * unless they are moderators/admins.
     */
    fun getEventsByUserId(userId: UUID, requestingUserFirebaseUid: String): List<Event> {
        logger.debug { "Getting events for user id: $userId requested by: $requestingUserFirebaseUid" }
        val targetUser = userService.getUserEntityById(userId)
        
        if (targetUser == null) {
            logger.warn { "User not found: $userId requested by: $requestingUserFirebaseUid" }
            return emptyList()
        }
        
        val allEvents = eventRepository.findAllByCreatorId(userId)
        val isModeratorOrAdmin = authorizationService.isModeratorOrAdmin(requestingUserFirebaseUid)
        val isOwnEvents = targetUser.firebaseUid == requestingUserFirebaseUid
        
        val filteredEvents = if (isModeratorOrAdmin || isOwnEvents) {
            // Moderators/admins and the user themselves can see all their events including hidden ones
            logger.debug { "Returning all ${allEvents.size} events for user: $userId (requested by: $requestingUserFirebaseUid)" }
            allEvents
        } else {
            // Other users can only see non-hidden events
            val visibleEvents = allEvents.filter { !it.isHidden }
            logger.debug { "Returning ${visibleEvents.size} visible events out of ${allEvents.size} for user: $userId (requested by: $requestingUserFirebaseUid)" }
            visibleEvents
        }
        
        return filteredEvents
    }
}
