package com.friendspark.backend.dto.event

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.util.UUID

/**
 * DTO for creating an Event. Accepts an ISO-8601 date string for eventDate which
 * will be parsed into an Instant in the controller/service layer.
 */
 data class CreateEventRequest(
    @field:NotBlank
    @field:Size(max = 100)
    val title: String,

    @field:NotBlank
    @field:Size(min = 5, max = 12)
    val locationGeohash: String,

    val description: String? = null,

    @field:NotBlank
    val eventDate: String, // ISO-8601 string, e.g. 2025-12-01T18:00:00Z

    val maxAttendees: Int? = null,

    // Nullable in input; default to true if not provided
    val isPublic: Boolean? = true,

    @field:NotNull
    var creatorId: UUID
 )
