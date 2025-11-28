package com.friendspark.backend.service

import com.friendspark.backend.dto.UserDetailsDTO
import com.friendspark.backend.entity.User
import com.friendspark.backend.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService(private val userRepository: UserRepository) {
    fun getAllUsers(): List<UserDetailsDTO> = userRepository.findAll().map { it.toDetailsDTO() }
    fun getUserById(id: UUID): UserDetailsDTO? = userRepository.findById(id).orElse(null)?.toDetailsDTO()
    fun getUserEntityById(id: UUID): User? = userRepository.findById(id).orElse(null)

    @Transactional
    fun registerIfNotExists(
        firebaseUid: String,
        email: String,
        name: String,
        interests: List<String>,
        geohash: String,
        latitude: Double,
        longitude: Double,
    ): User {
        val existing = userRepository.findByFirebaseUid(firebaseUid)
        if (existing != null) {
            return existing
        }

        val user = User(
            firebaseUid = firebaseUid,
            email = email,
            name = name,
            interests = interests,
            geohash = geohash,
            latitude = latitude,
            longitude = longitude,
            isOnboarded = true
        )

        return userRepository.save(user)
    }

    fun deleteUser(id: UUID) = userRepository.deleteById(id)

    fun save(user: User): User = userRepository.save(user)

    fun findByFirebaseUid(firebaseUid: String): User? = userRepository.findByFirebaseUid(firebaseUid)

    fun findNearbyUsers(geohashPrefix: String): List<UserDetailsDTO> =
        userRepository.findAllByGeohashStartingWith(geohashPrefix).map { it.toDetailsDTO() }
}

// Extension function to map User to UserDetailsDTO
private fun User.toDetailsDTO() = UserDetailsDTO(
    id = this.id,
    email = this.email,
    name = this.name,
    photoUrl = this.photoUrl,
    interests = this.interests,
    birthDate = this.birthDate,
)
