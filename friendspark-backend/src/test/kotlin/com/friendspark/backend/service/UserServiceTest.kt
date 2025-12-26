package com.friendspark.backend.service

import com.friendspark.backend.dto.RegisterRequestDTO
import com.friendspark.backend.dto.user.UserCreateDto
import com.friendspark.backend.entity.User
import com.friendspark.backend.entity.UserRole
import com.friendspark.backend.mapper.UserMapper
import com.friendspark.backend.repository.UserRepository
import com.friendspark.backend.util.DateTimeUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseToken
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.mockkStatic
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.time.Instant
import java.util.*

@ExtendWith(MockKExtension::class)
class UserServiceTest {

    @MockK
    lateinit var userRepository: UserRepository

    @MockK
    lateinit var userMapper: UserMapper

    @MockK
    lateinit var dateTimeUtil: DateTimeUtil

    @InjectMockKs
    lateinit var userService: UserService

    @Test
    fun `registerIfNotExists should return existing user when found`() {
        mockkStatic(FirebaseAuth::class)
        val mockFirebaseAuth = mockk<FirebaseAuth>()
        val mockToken = mockk<FirebaseToken>()
        every { FirebaseAuth.getInstance() } returns mockFirebaseAuth
        every { mockFirebaseAuth.verifyIdToken(any()) } returns mockToken
        every { mockToken.email } returns "user@test.com"
        every { mockToken.uid } returns "firebase-123"

        val name = "John Doe"
        val authHeader = "Bearer test-token"

        val requestDto = RegisterRequestDTO(name = name)

        val existingUser = User(
            id = UUID.randomUUID(),
            firebaseUid = "firebase-123",
            email = "user@test.com",
            firstName = "John",
            lastName = "Doe",
            geohash = "u09tunq9",
            createdAt = Instant.now(),
            updatedAt = Instant.now(),
            lastActiveAt = Instant.now(),
            role = UserRole.USER,
            isVerified = false,
            isBanned = false,
            bannedAt = null,
            createdEvents = null,
            rsvps = null,
            interests = null,
        )

        every { userRepository.findByFirebaseUid(any()) } returns existingUser

        val result = userService.registerIfNotExists(requestDto, authHeader)

        assertEquals(existingUser.id, result.id)
        assertEquals(existingUser.email, result.email)
    }

    @Test
    fun `registerIfNotExists should create new user when not found`() {
        mockkStatic(FirebaseAuth::class)
        val mockFirebaseAuth = mockk<FirebaseAuth>()
        val mockToken = mockk<FirebaseToken>()
        every { FirebaseAuth.getInstance() } returns mockFirebaseAuth
        every { mockFirebaseAuth.verifyIdToken(any()) } returns mockToken
        every { mockToken.email } returns "user@test.com"
        every { mockToken.uid } returns "firebase-123"

        val name = "Friend Sparker"
        val authHeader = "Bearer test-token"
        val firebaseUid = "firebase-123"
        val email = "user@test.com"
        val role = UserRole.USER
        val geohash = "u09tunq9"
        val now = Instant.now()

        val requestDto = RegisterRequestDTO(name = name)

        every { userRepository.findByFirebaseUid(any()) } returns null
        every { dateTimeUtil.now() } returns now

        val userCreateDto = UserCreateDto(
            firebaseUid = firebaseUid,
            email = email,
            firstName = "Friend",
            lastName = "Sparker"
        )

        val mappedUser = User(
            id = UUID.randomUUID(),
            firebaseUid = firebaseUid,
            email = email,
            firstName = "Friend",
            lastName = "Sparker",
            geohash = geohash,
            createdAt = now,
            updatedAt = now,
            lastActiveAt = now,
            role = role,
            isVerified = false,
            isBanned = false,
            bannedAt = null,
            createdEvents = null,
            rsvps = null,
            interests = null
        )

        every { userMapper.to(any()) } returns mappedUser
        every { userRepository.save(mappedUser) } returns mappedUser

        // Simulate service using userWriteDto internally after RegisterRequestDTO
        // If your service expects RegisterRequestDTO, you may need to stub the conversion logic

        val result = userService.registerIfNotExists(requestDto, authHeader)

        assertNotNull(result.id)
        assertEquals(mappedUser.email, result.email)
    }
}
