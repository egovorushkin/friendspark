package com.friendspark.backend.dto.event

import jakarta.validation.constraints.Size

/** Partial update DTO. All fields optional; null means 'no change'. */
 data class UpdateEventRequest(
    @field:Size(max = 100)
    val title: String? = null,
    @field:Size(min = 5, max = 12)
    val locationGeohash: String? = null,
    val description: String? = null,
    val eventDate: String? = null, // ISO-8601
    val maxAttendees: Int? = null,
    val isPublic: Boolean? = null
 )
