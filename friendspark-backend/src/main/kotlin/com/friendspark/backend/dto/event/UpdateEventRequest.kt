package com.friendspark.backend.dto.event

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.Size

/** Partial update DTO. All fields optional; null means 'no change'. */
data class UpdateEventRequest(
    @field:Size(max = 100)
    val title: String? = null,
    @field:Min(-90)
    @field:Max(90)
    val latitude: Double? = null,
    @field:Min(-180)
    @field:Max(180)
    val longitude: Double? = null,
    val description: String? = null,
    val eventDate: String? = null, // ISO-8601
    @field:Max(100)
    val maxAttendees: Int? = null,
    val isPublic: Boolean? = null
)
