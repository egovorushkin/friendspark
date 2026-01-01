package com.friendspark.backend.dto.user

import jakarta.validation.constraints.NotNull
import java.util.UUID

/**
 * Request DTO for promoting a user to admin role.
 */
data class PromoteToAdminRequest(
    @field:NotNull(message = "User ID is required")
    var userId: UUID
)

