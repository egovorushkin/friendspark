package com.friendspark.backend.dto.user

import com.friendspark.backend.entity.Gender
import com.friendspark.backend.entity.Interest

data class UserUpdateDTO(
    val firstName: String?,
    val lastName: String?,
    val photoUrl: String?,
    val latitude: Double?,
    val longitude: Double?,
    val birthDate: String?,
    val bio: String?,
    val gender: Gender,
    val interests: MutableSet<Interest>?
)
