//package com.friendspark.backend.service
//
//import com.friendspark.backend.dto.RegisterRequestDTO
//import com.friendspark.backend.dto.user.UserCreateDto
//import com.friendspark.backend.entity.User
//import com.friendspark.backend.entity.UserRole
//import com.friendspark.backend.mapper.UserMapper
//import com.friendspark.backend.repository.UserRepository
//import com.friendspark.backend.util.DateTimeUtil
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.auth.FirebaseToken
//import io.mockk.every
//import io.mockk.impl.annotations.InjectMockKs
//import io.mockk.impl.annotations.MockK
//import io.mockk.junit5.MockKExtension
//import io.mockk.mockk
//import io.mockk.mockkStatic
//import org.junit.jupiter.api.Assertions.assertEquals
//import org.junit.jupiter.api.Assertions.assertNotNull
//import org.junit.jupiter.api.Test
//import org.junit.jupiter.api.extension.ExtendWith
//import java.time.Instant
//import java.util.*
//import kotlin.collections.mutableSetOf
//
//@ExtendWith(MockKExtension::class)
//class UserServiceTest {
//    @MockK
//    lateinit var userRepository: UserRepository
//
//    @MockK
//    lateinit var userMapper: UserMapper
//
//    @MockK
//    lateinit var dateTimeUtil: DateTimeUtil
//
//    @InjectMockKs
//    lateinit var userService: UserService
//
//    @Test
//    fun `registerIfNotExists should return existing user when found`() {
//        mockkStatic(FirebaseAuth::class)
//        val mockFirebaseAuth = mockk<FirebaseAuth>()
//        val mockToken = mockk<FirebaseToken>()
//        every { FirebaseAuth.getInstance() } returns mockFirebaseAuth
//        every { mockFirebaseAuth.verifyIdToken(any()) } returns mockToken
//        every { mockToken.email } returns "user@test.com"
//        every { mockToken.uid } returns "firebase-123"
//
//        val name = "John Doe"
//        val authHeader = "Bearer test-token"
//        val now = Instant.now()
//        val existingUser = User(
//            id = UUID.randomUUID(),
//            firebaseUid = "firebase-123",
//            email = "user@test.com",
//            firstName = "John",
//            lastName = "Doe",
//            photoUrl = null,
//            role = UserRole.USER,
//            isVerified = false,
//            isBanned = false,
//            bannedReason = null,
//            bannedBy = null,
//            bannedAt = null,
//            geohash = "u09tunq9",
//            latitude = 0.0,
//            longitude = 0.0,
//            birthDate = null,
//            bio = null,
//            gender = null,
//            lastActiveAt = now,
//            createdEvents = mutableSetOf(),
//            rsvps = mutableSetOf(),
//            interests = mutableSetOf(),
//            createdAt = now,
//            updatedAt = now
//        )
//
//        every { userRepository.findByFirebaseUid(any()) } returns existingUser
//
//        val result = userService.registerIfNotExists(requestDto, authHeader)
//
//        assertEquals(existingUser.id, result.id)
//        assertEquals(existingUser.email, result.email)
//        assertEquals(existingUser.firstName, result.firstName)
//        assertEquals(existingUser.lastName, result.lastName)
//        assertEquals(existingUser.role, result.role)
//        assertEquals(existingUser.geohash, result.geohash)
//        assertEquals(existingUser.createdAt, result.createdAt)
//        assertEquals(existingUser.updatedAt, result.updatedAt)
//        assertEquals(existingUser.lastActiveAt, result.lastActiveAt)
//    }
//
//    @Test
//    fun `registerIfNotExists should create new user when not found`() {
//        mockkStatic(FirebaseAuth::class)
//        val mockFirebaseAuth = mockk<FirebaseAuth>()
//        val mockToken = mockk<FirebaseToken>()
//        every { FirebaseAuth.getInstance() } returns mockFirebaseAuth
//        every { mockFirebaseAuth.verifyIdToken(any()) } returns mockToken
//        every { mockToken.email } returns "user@test.com"
//        every { mockToken.uid } returns "firebase-123"
//
//        val name = "Friend Sparker"
//        val authHeader = "Bearer test-token"
//        val firebaseUid = "firebase-123"
//        val email = "user@test.com"
//        val role = UserRole.USER
//        val geohash = "u09tunq9"
//        val now = Instant.now()
//
//        val requestDto = RegisterRequestDTO(name = name)
//
//        every { userRepository.findByFirebaseUid(any()) } returns null
//        every { dateTimeUtil.now() } returns now
//
//        val userCreateDto = UserCreateDto(
//            firebaseUid = firebaseUid,
//            email = email,
//            firstName = "Friend",
//            lastName = "Sparker"
//        )
//
//        val mappedUser = User(
//            id = UUID.randomUUID(),
//            firebaseUid = firebaseUid,
//            email = email,
//            firstName = "Friend",
//            lastName = "Sparker",
//            photoUrl = null,
//            role = role,
//            isVerified = false,
//            isBanned = false,
//            bannedReason = null,
//            bannedBy = null,
//            bannedAt = null,
//            geohash = geohash,
//            latitude = 0.0,
//            longitude = 0.0,
//            birthDate = null,
//            bio = null,
//            gender = null,
//            lastActiveAt = now,
//            createdEvents = mutableSetOf<com.friendspark.backend.entity.Event>(),
//            rsvps = mutableSetOf<com.friendspark.backend.entity.UserEvent>(),
//            interests = mutableSetOf<com.friendspark.backend.entity.Interest>(),
//            createdAt = now,
//            updatedAt = now
//        )
//
//        every { userMapper.to(any<UserCreateDto>()) } returns mappedUser
//        every { userRepository.save(mappedUser) } returns mappedUser
//
//        val result = userService.registerIfNotExists(requestDto, authHeader)
//
//        assertNotNull(result.id)
//        assertEquals(mappedUser.email, result.email)
//        assertEquals(mappedUser.firstName, result.firstName)
//        assertEquals(mappedUser.lastName, result.lastName)
//        assertEquals(mappedUser.role, result.role)
//        assertEquals(mappedUser.geohash, result.geohash)
//        assertEquals(mappedUser.createdAt, result.createdAt)
//        assertEquals(mappedUser.updatedAt, result.updatedAt)
//        assertEquals(mappedUser.lastActiveAt, result.lastActiveAt)
//    }
//}
