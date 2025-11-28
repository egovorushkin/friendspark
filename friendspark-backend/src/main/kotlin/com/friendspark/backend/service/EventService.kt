package com.friendspark.backend.service

import com.friendspark.backend.dto.event.UpdateEventRequest
import com.friendspark.backend.entity.Event
import com.friendspark.backend.repository.EventRepository
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*

@Service
class EventService(private val eventRepository: EventRepository) {
    fun getAllEvents(): List<Event> {
        // TODO: add check that user exists and allowed to list events
        return eventRepository.findAll()
    }

    fun getEventById(id: UUID): Event? {
        // TODO: add check that user exists and allowed to list events
        return eventRepository.findById(id).orElse(null)
    }

    fun createEvent(event: Event): Event {
        // TODO: add check that user exists and allowed to create event
        return eventRepository.save(event)
    }

    fun deleteEvent(id: UUID) = eventRepository.deleteById(id)
    fun updateEvent(existing: Event, patch: UpdateEventRequest): Event {
        // TODO: add check that user exists and allowed to update event
        patch.title?.let { existing.title = it }
        patch.locationGeohash?.let { existing.locationGeohash = it }
        patch.description?.let { existing.description = it }
        patch.eventDate?.let { existing.eventDate = Instant.parse(it) }
        patch.maxAttendees?.let { existing.maxAttendees = it }
        patch.isPublic?.let { existing.isPublic = it }
        return eventRepository.save(existing)
    }
}
