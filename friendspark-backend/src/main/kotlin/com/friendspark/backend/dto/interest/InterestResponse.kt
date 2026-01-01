package com.friendspark.backend.dto.interest

import java.util.UUID

data class InterestResponse(
    val id: UUID,
    val name: String,
    val iconName: String? = null,
)

