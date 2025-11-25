package com.friendspark.backend.dto.event

import com.friendspark.backend.entity.Event
import java.time.Instant
import java.util.UUID

/**
 * DTO sent back to clients; hides lazy JPA relationships and formats data cleanly.
 */
 data class EventResponse(
    val id: UUID?,
    val title: String,
    val locationGeohash: String,
    val description: String?,
    val eventDate: Instant,
    val maxAttendees: Int?,
    val isPublic: Boolean,
    val creatorId: UUID,
    val createdAt: Instant,
    val updatedAt: Instant
 ) {
    companion object {
        fun fromEntity(e: Event): EventResponse = EventResponse(
            id = e.id,
            title = e.title,
            locationGeohash = e.locationGeohash,
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
