package com.friendspark.backend.mapper

import com.friendspark.backend.dto.event.EventCreateDTO
import com.friendspark.backend.dto.event.UpdateEventRequest
import com.friendspark.backend.entity.Event
import com.friendspark.backend.entity.User
import com.friendspark.backend.util.DateTimeUtil
import com.friendspark.backend.util.Geohash
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class EventMapper(private val dateTimeUtil: DateTimeUtil) {
    fun toEntity(dto: EventCreateDTO, creator: User): Event =
        Event(
            title = dto.title,
            latitude = dto.latitude,
            longitude = dto.longitude,
            geohash = Geohash.encode(dto.latitude, dto.longitude),
            description = dto.description,
            eventDate = Instant.parse(dto.eventDate),
            maxAttendees = dto.maxAttendees,
            isPublic = dto.isPublic ?: true,
            createdAt = dateTimeUtil.now(),
            updatedAt = dateTimeUtil.now(),
            creator = creator
        )

    fun update(existing: Event, patch: UpdateEventRequest): Event = existing.apply {
        patch.title?.let { title = it }
        patch.latitude?.let { latitude = it }
        patch.longitude?.let { longitude = it }
        if (patch.latitude != null && patch.longitude != null) {
            geohash = Geohash.encode(patch.latitude, patch.longitude)
        }
        patch.description?.let { description = it }
        patch.eventDate?.let { eventDate = Instant.parse(it) }
        patch.maxAttendees?.let { maxAttendees = it }
        patch.isPublic?.let { isPublic = it }
        updatedAt = dateTimeUtil.now()
    }
}