package com.friendspark.backend.service

import com.friendspark.backend.dto.RegisterRequestDTO
import com.friendspark.backend.dto.user.UserCreateDto
import com.friendspark.backend.dto.user.UserDetailsDTO
import com.friendspark.backend.dto.user.UserUpdateDTO
import com.friendspark.backend.entity.User
import com.friendspark.backend.entity.UserRole
import com.friendspark.backend.mapper.UserMapper
import com.friendspark.backend.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import jakarta.transaction.Transactional
import mu.KotlinLogging
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService(
    private val userMapper: UserMapper,
    private val userRepository: UserRepository,
    private val firebaseService: FirebaseService,
    private val interestService: InterestService,
) {
    private val logger = KotlinLogging.logger {}

    fun getAllUsers(pageable: Pageable): Page<UserDetailsDTO> {
        logger.debug { "Retrieving all users with pagination: page=${pageable.pageNumber}, size=${pageable.pageSize}" }
        val userPage = userRepository.findAll(pageable)
        logger.debug { "Found ${userPage.totalElements} total users, returning page ${userPage.number + 1} of ${userPage.totalPages}" }
        return userPage.map { userMapper.toDetailsDTO(it) }
    }
    
    fun getUserById(id: UUID): UserDetailsDTO? {
        logger.debug { "Getting user by id: $id" }
        val user = userRepository.findById(id).orElse(null)
        return if (user != null) {
            logger.debug { "User found: $id (email: ${user.email})" }
            userMapper.toDetailsDTO(user)
        } else {
            logger.debug { "User not found: $id" }
            null
        }
    }

    fun getUserEntityById(id: UUID): User? {
        logger.debug { "Getting user entity by id: $id" }
        return userRepository.findById(id).orElse(null)
    }

    @Transactional
    fun registerIfNotExists(
        request: RegisterRequestDTO,
        authHeader: String,
    ): User {
        logger.debug { "Processing registration for user: ${request.name}" }
        
        // todo: refactoring
        val token = authHeader.removePrefix("Bearer ").trim()
        val decodedToken = try {
            FirebaseAuth.getInstance().verifyIdToken(token)
        } catch (e: Exception) {
            logger.error(e) { "Failed to verify Firebase token during registration" }
            throw e
        }
        
        val email = decodedToken.email ?: throw IllegalArgumentException("Email not found in token")
        val name = request.name
        val firebaseUid = decodedToken.uid
        logger.debug { "Firebase UID extracted: $firebaseUid for email: $email" }
        
        val existing = userRepository.findByFirebaseUid(firebaseUid)
        if (existing != null) {
            logger.info { "User already exists with Firebase UID: $firebaseUid (id: ${existing.id})" }
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
        logger.info { "New user created: $email (id: ${savedUser.id}, firebaseUid: $firebaseUid)" }

        try {
            firebaseService.addCustomClaims(savedUser.firebaseUid, mapOf("role" to savedUser.role.name))
            logger.debug { "Custom claims added to Firebase for user: $firebaseUid" }
        } catch (e: Exception) {
            logger.error(e) { "Failed to add custom claims to Firebase for user: $firebaseUid" }
            // Continue even if custom claims fail - user is already created
        }
        
        return savedUser
    }

    fun deleteUser(id: UUID) {
        logger.info { "Deleting user: $id" }
        userRepository.deleteById(id)
        logger.debug { "User deleted: $id" }
    }

    fun save(user: User): User {
        logger.debug { "Saving user: ${user.id} (email: ${user.email})" }
        return userRepository.save(user)
    }

    fun findByFirebaseUid(firebaseUid: String): User? {
        logger.debug { "Finding user by Firebase UID: $firebaseUid" }
        return userRepository.findByFirebaseUid(firebaseUid)
    }

    fun findNearbyUsers(geohashPrefix: String): List<UserDetailsDTO> {
        logger.debug { "Finding nearby users with geohash prefix: $geohashPrefix" }
        val users = userRepository.findAllByGeohashStartingWith(geohashPrefix)
        logger.debug { "Found ${users.size} users with geohash prefix: $geohashPrefix" }
        return users.map { userMapper.toDetailsDTO(it) }
    }

    fun updateUserProfile(userId: String, updateDTO: UserUpdateDTO): UserDetailsDTO {
        logger.info { "Updating profile for Firebase UID: $userId" }
        val user = userRepository.findByFirebaseUid(userId) ?: run {
            logger.error { "User not found for Firebase UID: $userId" }
            throw IllegalArgumentException("User not found")
        }

        // Map simple scalar fields
        userMapper.update(user, updateDTO)

        // Resolve and set interests (if provided)
        updateDTO.interestIds?.let { ids ->
            val interests = interestService.resolveByIds(ids)
            user.interests = interests.toMutableSet()
        }

        val updatedUser = userRepository.save(user)
        logger.info { "Profile updated successfully for user: $userId (id: ${updatedUser.id})" }
        return userMapper.toDetailsDTO(updatedUser)
    }

    @Transactional
    fun promoteToAdmin(userId: UUID, promotedByFirebaseUid: String): UserDetailsDTO {
        logger.info("Promoting user {} to admin by: {}", userId, promotedByFirebaseUid)
        val user = userRepository.findById(userId).orElse(null) ?: run {
            logger.error("User not found for promotion: {} by: {}", userId, promotedByFirebaseUid)
            throw IllegalArgumentException("User not found")
        }

        if (user.role == UserRole.ADMIN) {
            logger.warn("User {} is already an admin, requested by: {}", userId, promotedByFirebaseUid)
            return userMapper.toDetailsDTO(user)
        }

        val previousRole = user.role
        user.role = UserRole.ADMIN
        val updatedUser = userRepository.save(user)
        logger.info("User {} promoted from {} to ADMIN by: {}", userId, previousRole, promotedByFirebaseUid)

        // Update Firebase custom claims
        try {
            firebaseService.addCustomClaims(updatedUser.firebaseUid, mapOf("role" to updatedUser.role.name))
            logger.debug("Firebase custom claims updated for user: {} (role: ADMIN)", updatedUser.firebaseUid)
        } catch (e: Exception) {
            logger.error("Failed to update Firebase custom claims for user: {}", updatedUser.firebaseUid, e)
            // Continue even if Firebase update fails - role is already updated in database
        }

        return userMapper.toDetailsDTO(updatedUser)
    }
}
