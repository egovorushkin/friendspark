package com.friendspark.backend.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.validation.constraints.NotNull
import org.hibernate.annotations.ColumnDefault
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "user_reports")
class UserReport(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    var id: UUID? = null,

    @ManyToOne
    @JoinColumn(name = "reporter_id", nullable = false)
    val reporter: User,

    @ManyToOne
    @JoinColumn(name = "reported_user_id", nullable = false)
    val reportedUser: User,

    @Column(nullable = false)
    var reason: String,

    var status: String = "PENDING", // PENDING, REVIEWED, ACTION_TAKEN

    var reviewedBy: UUID? = null,

    @NotNull
    @ColumnDefault("(now) AT TIME ZONE 'utc'::text")
    @Column(name = "created_at", nullable = false)
    var createdAt: Instant,

    @NotNull
    @ColumnDefault("(now) AT TIME ZONE 'utc'::text")
    @Column(name = "updated_at", nullable = false)
    var updatedAt: Instant,
)