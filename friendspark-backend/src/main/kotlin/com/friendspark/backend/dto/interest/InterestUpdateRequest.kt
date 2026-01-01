package com.friendspark.backend.dto.interest

import jakarta.validation.constraints.Size

data class InterestUpdateRequest(
    @field:Size(max = 50)
    val name: String? = null,
    @field:Size(max = 50)
    val iconName: String? = null,
)

