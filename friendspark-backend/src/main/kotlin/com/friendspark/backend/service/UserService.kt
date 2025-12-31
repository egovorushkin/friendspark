package com.friendspark.backend.service

import com.friendspark.backend.dto.RegisterRequestDTO
import com.friendspark.backend.dto.user.UserCreateDto
import com.friendspark.backend.dto.user.UserDetailsDTO
import com.friendspark.backend.dto.user.UserUpdateDTO
import com.friendspark.backend.entity.User
import com.friendspark.backend.mapper.UserMapper
import com.friendspark.backend.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService(
    private val userMapper: UserMapper,
    private val userRepository: UserRepository,
    private val firebaseService: FirebaseService,
) {
    private val log = LoggerFactory.getLogger(javaClass)
    fun getAllUsers(): List<UserDetailsDTO> {
        log.debug("Retrieving all users")
        val users = userRepository.findAll()
        log.debug("Found {} users in database", users.size)
        return users.map { userMapper.toDetailsDTO(it) }
    }
    
    fun getUserById(id: UUID): UserDetailsDTO? {
        log.debug("Getting user by id: {}", id)
        val user = userRepository.findById(id).orElse(null)
        return if (user != null) {
            log.debug("User found: {} (email: {})", id, user.email)
            userMapper.toDetailsDTO(user)
        } else {
            log.debug("User not found: {}", id)
            null
        }
    }

    fun getUserEntityById(id: UUID): User? {
        log.debug("Getting user entity by id: {}", id)
        return userRepository.findById(id).orElse(null)
    }

    @Transactional
    fun registerIfNotExists(
        request: RegisterRequestDTO,
        authHeader: String,
    ): User {
        log.debug("Processing registration for user: {}", request.name)
        
        // todo: refactoring
        val token = authHeader.removePrefix("Bearer ").trim()
        val decodedToken = try {
            FirebaseAuth.getInstance().verifyIdToken(token)
        } catch (e: Exception) {
            log.error("Failed to verify Firebase token during registration", e)
            throw e
        }
        
        val email = decodedToken.email ?: throw IllegalArgumentException("Email not found in token")
        val name = request.name
        val firebaseUid = decodedToken.uid
        log.debug("Firebase UID extracted: {} for email: {}", firebaseUid, email)
        
        val existing = userRepository.findByFirebaseUid(firebaseUid)
        if (existing != null) {
            log.info("User already exists with Firebase UID: {} (id: {})", firebaseUid, existing.id)
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
        val newUser = userMapper.toEntity(userCreate)

        val savedUser = userRepository.save(newUser)
        log.info("New user created: {} (id: {}, firebaseUid: {})", email, savedUser.id, firebaseUid)

        try {
            firebaseService.addCustomClaims(savedUser.firebaseUid, mapOf("role" to savedUser.role.name))
            log.debug("Custom claims added to Firebase for user: {}", firebaseUid)
        } catch (e: Exception) {
            log.error("Failed to add custom claims to Firebase for user: {}", firebaseUid, e)
            // Continue even if custom claims fail - user is already created
        }
        
        return savedUser
    }

    fun deleteUser(id: UUID) {
        log.info("Deleting user: {}", id)
        userRepository.deleteById(id)
        log.debug("User deleted: {}", id)
    }

    fun save(user: User): User {
        log.debug("Saving user: {} (email: {})", user.id, user.email)
        return userRepository.save(user)
    }

    fun findByFirebaseUid(firebaseUid: String): User? {
        log.debug("Finding user by Firebase UID: {}", firebaseUid)
        return userRepository.findByFirebaseUid(firebaseUid)
    }

    fun findNearbyUsers(geohashPrefix: String): List<UserDetailsDTO> {
        log.debug("Finding nearby users with geohash prefix: {}", geohashPrefix)
        val users = userRepository.findAllByGeohashStartingWith(geohashPrefix)
        log.debug("Found {} users with geohash prefix: {}", users.size, geohashPrefix)
        return users.map { userMapper.toDetailsDTO(it) }
    }

    fun updateUserProfile(userId: String, updateDTO: UserUpdateDTO): UserDetailsDTO {
        log.info("Updating profile for Firebase UID: {}", userId)
        val user = userRepository.findByFirebaseUid(userId) ?: run {
            log.error("User not found for Firebase UID: {}", userId)
            throw IllegalArgumentException("User not found")
        }
        val userUpdate = userMapper.update(user, updateDTO)
        val updatedUser = userRepository.save(userUpdate)
        log.info("Profile updated successfully for user: {} (id: {})", userId, updatedUser.id)
        return userMapper.toDetailsDTO(updatedUser)
    }
}
