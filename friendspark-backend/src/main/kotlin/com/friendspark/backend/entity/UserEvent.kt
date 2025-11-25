package com.friendspark.backend.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.IdClass
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "user_events")
@IdClass(UserEventId::class)
data class UserEvent(
    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: User,

    @Id
    @ManyToOne
    @JoinColumn(name = "event_id")
    val event: Event,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var rsvpStatus: RsvpStatus = RsvpStatus.GOING,

    @Column(name = "joined_at", nullable = false)
    val joinedAt: Instant = Instant.now()
)

enum class RsvpStatus {
    GOING, MAYBE, NOT_GOING
}

// Composite key for UserEvent
data class UserEventId(
    val user: UUID = UUID.randomUUID(),
    val event: UUID = UUID.randomUUID()
) : java.io.Serializable