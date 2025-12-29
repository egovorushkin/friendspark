package com.friendspark.backend.dto.event

import jakarta.validation.constraints.NotBlank
import java.util.*

/**
 * DTO for response of created Event. Contains the generated event ID.
 */
data class EventCreateResponseDTO(
    @field:NotBlank
    val id: UUID?
)
