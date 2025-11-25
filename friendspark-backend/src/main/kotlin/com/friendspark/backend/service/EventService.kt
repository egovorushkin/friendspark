package com.friendspark.backend.service

import com.friendspark.backend.dto.event.UpdateEventRequest
import com.friendspark.backend.entity.Event
import com.friendspark.backend.repository.EventRepository
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.UUID

@Service
class EventService(private val eventRepository: EventRepository) {
    fun getAllEvents(): List<Event> = eventRepository.findAll()
    fun getEventById(id: UUID): Event? = eventRepository.findById(id).orElse(null)
    fun createEvent(event: Event): Event = eventRepository.save(event)
    fun deleteEvent(id: UUID) = eventRepository.deleteById(id)
    fun updateEvent(existing: Event, patch: UpdateEventRequest): Event {
        patch.title?.let { existing.title = it }
        patch.locationGeohash?.let { existing.locationGeohash = it }
        patch.description?.let { existing.description = it }
        patch.eventDate?.let { existing.eventDate = Instant.parse(it) }
        patch.maxAttendees?.let { existing.maxAttendees = it }
        patch.isPublic?.let { existing.isPublic = it }
        return eventRepository.save(existing)
    }
}
