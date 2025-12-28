package com.friendspark.backend.dto.event

import com.friendspark.backend.entity.Event
import java.time.Instant
import java.util.*

/**
 * DTO sent back to clients; hides lazy JPA relationships and formats data cleanly.
 */
data class EventDetailsDTO(
    val id: UUID?,
    val title: String,
    val geohash: String,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val description: String?,
    val eventDate: Instant,
    val maxAttendees: Int?,
    val isPublic: Boolean,
    val creatorId: UUID?,
    val createdAt: Instant,
    val updatedAt: Instant
) {
    companion object {
        fun fromEntity(e: Event): EventDetailsDTO = EventDetailsDTO(
            id = e.id,
            title = e.title,
            geohash = e.geohash,
            latitude = e.latitude,
            longitude = e.longitude,
            description = e.description,
            eventDate = e.eventDate,
            maxAttendees = e.maxAttendees,
            isPublic = e.isPublic,
            creatorId = e.creator.id,
            createdAt = e.createdAt,
            updatedAt = e.updatedAt
        )
    }
}
