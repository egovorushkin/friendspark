package com.friendspark.backend.dto.event

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

/**
 * DTO for creating an Event. Accepts an ISO-8601 date string ('2025-12-01T18:00:00Z') for eventDate which
 * will be parsed into an Instant in the controller/service layer.
 */
data class EventCreateDTO(
    @field:NotBlank
    @field:Size(max = 100)
    val title: String,

    val latitude: Double,

    val longitude: Double,

    val description: String? = null,

    @field:NotBlank
    val eventDate: String,

    val maxAttendees: Int? = null,

    // Nullable in input; default to true if not provided
    val isPublic: Boolean? = true,

    )
