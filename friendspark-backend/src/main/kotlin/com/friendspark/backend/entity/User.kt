package com.friendspark.backend.entity

import jakarta.persistence.Column
import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.Table
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "users", indexes = [
    Index(columnList = "geohash"),           // fast nearby queries
    Index(columnList = "firebase_uid", unique = true)
])
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: UUID? = null,

    @Column(name = "firebase_uid", nullable = false, unique = true)
    val firebaseUid: String,

    @Column(nullable = false)
    val email: String,

    @Column(nullable = false)
    var name: String,

    var photoUrl: String? = null,

    @ElementCollection
    var interests: List<String> = emptyList(),

    @Column(length = 12, nullable = false)
    var geohash: String = "",

    var latitude: Double = 0.0,
    var longitude: Double = 0.0,

    var birthDate: LocalDate? = null,

    @Enumerated(EnumType.STRING)
    var gender: Gender? = null,

    @Column(nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    var lastActiveAt: LocalDateTime = LocalDateTime.now(),

    var isOnboarded: Boolean = false
)