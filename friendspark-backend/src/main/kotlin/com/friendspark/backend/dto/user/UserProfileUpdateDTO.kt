package com.friendspark.backend.dto.user

data class UserProfileUpdateDTO(
    val name: String?,
    val photoUrl: String?,
    val interests: List<String>?
)

