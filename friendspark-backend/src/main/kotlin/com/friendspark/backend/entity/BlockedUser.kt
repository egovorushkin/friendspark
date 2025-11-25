package com.friendspark.backend.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.IdClass
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "blocked_users")
@IdClass(BlockedUserId::class)
data class BlockedUser(
    @Id
    @ManyToOne
    @JoinColumn(name = "blocker_id")
    val blocker: User,

    @Id
    @ManyToOne
    @JoinColumn(name = "blocked_id")
    val blocked: User,

    @Column(name = "blocked_at", nullable = false)
    val blockedAt: Instant = Instant.now(),

    var reason: String? = null
)

data class BlockedUserId(
    val blocker: UUID = UUID.randomUUID(),
    val blocked: UUID = UUID.randomUUID()
) : java.io.Serializable