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
    private val eventMapper: EventMapper
) {
    fun getAllEvents(): List<Event> {
        return eventRepository.findAll()
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
    fun updateEvent(existing: Event, patch: UpdateEventRequest): Event {
        // TODO: add check that user exists and allowed to update event
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
