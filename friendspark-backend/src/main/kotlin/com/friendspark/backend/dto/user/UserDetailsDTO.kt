package com.friendspark.backend.dto.user

import com.friendspark.backend.entity.Gender
import java.time.LocalDate

class UserDetailsDTO(
    val email: String,
    val name: String,
    val photoUrl: String?,
    val latitude: Double?,
    val longitude: Double?,
    val birthDate: LocalDate?,
    val bio: String?,
    val gender: Gender?,
    val interests: List<String>,
)