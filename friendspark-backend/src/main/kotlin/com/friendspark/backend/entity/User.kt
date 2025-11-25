package com.friendspark.backend.entity

import jakarta.persistence.CollectionTable
import jakarta.persistence.Column
import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.JoinColumn
import jakarta.persistence.PreUpdate
import jakarta.persistence.Table
import java.time.Instant
import java.util.UUID

@Entity
@Table(
    name = "users",
    indexes = [
        Index(name = "idx_users_geohash", columnList = "geohash"),
        Index(name = "idx_users_email", columnList = "email", unique = true)
    ]
)
data class User(
    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid")
    val id: UUID = UUID.randomUUID(),

    @Column(nullable = false, unique = true, length = 255)
    val email: String,

    @Column(nullable = false)
    var passwordHash: String?,

    @Column(nullable = false, length = 100)
    var name: String,

    @Column(length = 20)
    var ageRange: String? = null, // e.g., "18-24"

    @ElementCollection
    @CollectionTable(name = "user_interests", joinColumns = [JoinColumn(name = "user_id")])
    @Column(name = "interest")
    var interests: MutableSet<String> = mutableSetOf(),

    @Column(nullable = false, length = 12)
    var geohash: String = "", // 9–12 chars = ~10m to ~1m precision

    @Column(name = "photo_url")
    var photoUrl: String? = null,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: Instant = Instant.now(),

    @Column(name = "updated_at")
    var updatedAt: Instant = Instant.now(),

    @Column(name = "is_verified", nullable = false)
    var isVerified: Boolean = false
) {
    @PreUpdate
    fun onUpdate() {
        updatedAt = Instant.now()
    }
}