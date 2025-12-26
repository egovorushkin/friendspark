package com.friendspark.backend.dto.user

class UserCreateDto(
    var firebaseUid: String,
    var email: String? = null,
    var firstName: String? = null,
    var lastName: String? = null,
)