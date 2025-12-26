package com.friendspark.backend.dto

import java.time.LocalDate
import java.util.UUID

class UserDetailsDTO(
    val id: UUID?,
    val email: String,
    val name: String,
    val photoUrl: String?,
    val interests: List<String>,
    val birthDate: LocalDate?
)

