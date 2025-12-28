package com.friendspark.backend.service

import com.friendspark.backend.dto.RegisterRequestDTO
import com.friendspark.backend.dto.user.UserDetailsDTO
import com.friendspark.backend.dto.user.UserCreateDto
import com.friendspark.backend.dto.user.UserUpdateDTO
import com.friendspark.backend.entity.User
import com.friendspark.backend.mapper.UserMapper
import com.friendspark.backend.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService(
    private val userMapper: UserMapper,
    private val userRepository: UserRepository,
    private val firebaseService: FirebaseService,
) {
    fun getAllUsers(): List<UserDetailsDTO> = userRepository.findAll().map { it.toDetailsDTO() }
    fun getUserById(id: UUID): UserDetailsDTO? = userRepository.findById(id).orElse(null)?.toDetailsDTO()
    fun getUserEntityById(id: UUID): User? = userRepository.findById(id).orElse(null)

    @Transactional
    fun registerIfNotExists(
        request: RegisterRequestDTO,
        authHeader: String,
    ): User {

        // todo: refactoring
        val token = authHeader.removePrefix("Bearer ").trim()
        val decodedToken = FirebaseAuth.getInstance().verifyIdToken(token)
        val email = decodedToken.email ?: throw IllegalArgumentException("Email not found in token")
        val name = request.name
        val firebaseUid = decodedToken.uid
        val existing = userRepository.findByFirebaseUid(firebaseUid)
        if (existing != null) {
            return existing
        }

        // Simple split of full name into first and last
        val parts = name.trim().split(" ", limit = 2)
        val firstName = parts.getOrNull(0) ?: name
        val lastName = parts.getOrNull(1) ?: ""

        val userCreate = UserCreateDto(
            firebaseUid = firebaseUid,
            email = email,
            firstName = firstName,
            lastName = lastName,
        )
        val newUser = userMapper.to(userCreate)

        val savedUser = userRepository.save(newUser)

        // TODO: handle exceptions
        firebaseService.addCustomClaims(savedUser.firebaseUid, mapOf("role" to savedUser.role.name))
        return savedUser
    }

    fun deleteUser(id: UUID) = userRepository.deleteById(id)

    fun save(user: User): User = userRepository.save(user)

    fun findByFirebaseUid(firebaseUid: String): User? = userRepository.findByFirebaseUid(firebaseUid)

    fun findNearbyUsers(geohashPrefix: String): List<UserDetailsDTO> =
        userRepository.findAllByGeohashStartingWith(geohashPrefix).map { it.toDetailsDTO() }

    fun updateUserProfile(userId: String, updateDTO: UserUpdateDTO): UserDetailsDTO {
        val user = userRepository.findByFirebaseUid(userId) ?: throw IllegalArgumentException("User not found")
        val userUpdate = userMapper.update(user, updateDTO)
        userRepository.save(userUpdate)
        return user.toDetailsDTO()
    }
}

// TODO: Move to Mapper
// Extension function to map User to UserDetailsDTO
private fun User.toDetailsDTO() = UserDetailsDTO(
    id = this.id,
    email = this.email,
    name = listOfNotNull(this.firstName, this.lastName).filter { it.isNotBlank() }.joinToString(" ").ifBlank { this.firstName },
    photoUrl = this.photoUrl,
    interests = this.interests?.map { it.toString() } ?: emptyList(),
    birthDate = this.birthDate,
)
