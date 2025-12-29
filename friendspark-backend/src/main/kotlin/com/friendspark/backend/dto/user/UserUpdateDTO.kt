package com.friendspark.backend.dto.user

import com.friendspark.backend.entity.Gender
import com.friendspark.backend.entity.Interest
import java.time.LocalDate

data class UserUpdateDTO(
    val firstName: String?,
    val lastName: String?,
    val photoUrl: String?,
    val latitude: Double?,
    val longitude: Double?,
    val birthDate: LocalDate?,
    val bio: String?,
    val gender: Gender?,
    val interests: MutableSet<Interest>?
)
