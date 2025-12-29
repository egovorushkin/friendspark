package com.friendspark.backend.mapper

import com.friendspark.backend.dto.event.EventCreateDTO
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
            geohash = Geohash.encode(dto.latitude, dto.longitude),
            latitude = dto.latitude,
            longitude = dto.longitude,
            description = dto.description,
            eventDate = Instant.parse(dto.eventDate),
            maxAttendees = dto.maxAttendees,
            isPublic = dto.isPublic ?: true,
            createdAt = dateTimeUtil.now(),
            updatedAt = dateTimeUtil.now(),
            creator = creator
        )
}