package com.friendspark.backend.dto.user

import com.friendspark.backend.entity.Interest
import java.time.LocalDate
import java.util.UUID

class UserDetailsDTO(
    val id: UUID?,
    val email: String,
    val firstName: String,
    val lastName: String,
    val photoUrl: String?,
    val interests: MutableSet<Interest>?,
    val birthDate: LocalDate?
)
