package com.friendspark.backend.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.PreUpdate
import jakarta.persistence.Table
import jakarta.persistence.Version
import java.time.Instant
import java.util.UUID

@Entity
@Table(
    name = "events",
    indexes = [
        Index(name = "idx_events_geohash", columnList = "location_geohash"),
        Index(name = "idx_events_date", columnList = "event_date")
    ]
)
data class Event(
    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid")
    val id: UUID? = null, // let JPA generate; do NOT pre-populate to avoid detached entity issues

    @Column(nullable = false, length = 100)
    var title: String,

    @Column(name = "location_geohash", nullable = false, length = 12)
    var locationGeohash: String,

    @Column(columnDefinition = "TEXT")
    var description: String? = null,

    @Column(name = "event_date", nullable = false)
    var eventDate: Instant,

    @Column(name = "max_attendees")
    var maxAttendees: Int? = null,

    @Column(name = "is_public", nullable = false)
    var isPublic: Boolean = true,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    val creator: User,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: Instant = Instant.now(),

    @Column(name = "updated_at")
    var updatedAt: Instant = Instant.now(),

    @Version
    @Column(name = "version", nullable = false)
    var version: Long = 0L
) {
    @PreUpdate
    fun onUpdate() {
        updatedAt = Instant.now()
    }
}