package com.friendspark.backend.dto.interest

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class InterestCreateRequest(
    @field:NotBlank
    @field:Size(max = 50)
    val name: String,
    @field:Size(max = 50)
    val iconName: String? = null,
)

