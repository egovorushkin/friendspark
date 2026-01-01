package com.friendspark.backend.mapper

import com.friendspark.backend.dto.user.UserCreateDto
import com.friendspark.backend.dto.user.UserDetailsDTO
import com.friendspark.backend.dto.user.UserUpdateDTO
import com.friendspark.backend.entity.User
import com.friendspark.backend.entity.UserRole
import com.friendspark.backend.util.DateTimeUtil
import org.springframework.stereotype.Component

@Component
class UserMapper(private val dateTimeUtil: DateTimeUtil) {
    fun toEntity(dto: UserCreateDto): User =
        User(
            firebaseUid = dto.firebaseUid,
            email = dto.email,
            firstName = dto.firstName,
            lastName = dto.lastName,
            lastActiveAt = dateTimeUtil.now(),
            createdAt = dateTimeUtil.now(),
            updatedAt = dateTimeUtil.now(),
            role = UserRole.USER,
        )

    fun update(user: User, dto: UserUpdateDTO): User = user.apply {
        dto.firstName?.let { firstName = it }
        dto.lastName?.let { lastName = it }
        dto.photoUrl?.let { photoUrl = it }
        dto.latitude?.let { latitude = it }
        dto.longitude?.let { longitude = it }
        dto.birthDate?.let { birthDate = it }
        dto.bio?.let { bio = it }
        dto.gender?.let { gender = it }
        lastActiveAt = dateTimeUtil.now()
        updatedAt = dateTimeUtil.now()
    }

    fun toDetailsDTO(user: User) = UserDetailsDTO(
        email = user.email,
        name = user.firstName + " " + user.lastName,
        photoUrl = user.photoUrl,
        latitude = user.latitude,
        longitude = user.longitude,
        birthDate = user.birthDate,
        bio = user.bio,
        gender = user.gender,
        interests = user.interests?.map { it.name } ?: emptyList(),
    )

}