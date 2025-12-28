package com.friendspark.backend.entity

import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import lombok.Getter
import lombok.Setter
import org.hibernate.annotations.ColumnDefault
import java.time.Instant
import java.util.*

/**
 * Represents an event created by a user.
 */
@Entity
@Getter
@Setter
@Table(
    schema = "friendspark",
    name = "events"
)
data class Event(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    var id: UUID? = null,

    @Column(nullable = false, length = 100)
    var title: String,

    @Column(length = 12, nullable = false)
    var geohash: String,

    var latitude: Double,
    var longitude: Double,

    var description: String? = null,

    @NotNull
    @ColumnDefault("(now) AT TIME ZONE 'utc'::text")
    @Column(name = "event_date", nullable = false)
    var eventDate: Instant,

    var durationMinutes: Int = 120,

    @Column(name = "max_attendees")
    var maxAttendees: Int? = null,

    @Column(name = "is_public", nullable = false)
    var isPublic: Boolean = true,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    val creator: User,

    // Moderation
    @Column(name = "is_hidden")
    var isHidden: Boolean = false,

    var hiddenBy: UUID? = null,

    var hiddenReason: String? = null,

    @OneToMany(mappedBy = "event", cascade = [CascadeType.ALL], orphanRemoval = true)
    val rsvps: MutableSet<UserEvent> = mutableSetOf(),

    @NotNull
    @ColumnDefault("(now) AT TIME ZONE 'utc'::text")
    @Column(name = "created_at", nullable = false)
    var createdAt: Instant,

    @NotNull
    @ColumnDefault("(now) AT TIME ZONE 'utc'::text")
    @Column(name = "updated_at", nullable = false)
    var updatedAt: Instant,
)