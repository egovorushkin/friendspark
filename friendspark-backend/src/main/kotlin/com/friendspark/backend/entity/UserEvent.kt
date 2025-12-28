package com.friendspark.backend.entity

import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import org.hibernate.annotations.ColumnDefault
import java.time.Instant

@Entity
@Table(schema = "friendspark", name = "user_events_rsvps")
@IdClass(UserEventId::class)
class UserEvent(
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    val event: Event,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: RsvpStatus = RsvpStatus.INTERESTED,

    @NotNull
    @ColumnDefault("(now) AT TIME ZONE 'utc'::text")
    @Column(name = "responded_at", nullable = false)
    var respondedAt: Instant
)
