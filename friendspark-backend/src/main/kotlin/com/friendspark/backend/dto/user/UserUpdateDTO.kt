package com.friendspark.backend.dto.user

import com.friendspark.backend.entity.Gender
import java.time.LocalDate
import java.util.UUID

data class UserUpdateDTO(
    val firstName: String?,
    val lastName: String?,
    val photoUrl: String?,
    val latitude: Double?,
    val longitude: Double?,
    val birthDate: LocalDate?,
    val bio: String?,
    val gender: Gender?,
    /**
     * Interest IDs to set for the user. If null, interests are not modified.
     * If empty set, user interests will be cleared.
     */
    val interestIds: Set<UUID>?,
)
