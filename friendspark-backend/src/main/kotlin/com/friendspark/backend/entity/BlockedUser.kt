package com.friendspark.backend.entity

import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import org.hibernate.annotations.ColumnDefault
import java.time.Instant

@Entity
@Table(name = "blocked_users")
@IdClass(BlockId::class)
class BlockedUser(
    @Id
    @ManyToOne
    @JoinColumn(name = "blocker_id")
    val blocker: User,

    @Id
    @ManyToOne
    @JoinColumn(name = "blocked_id")
    val blocked: User,

    var reason: String? = null,

    @NotNull
    @ColumnDefault("(now) AT TIME ZONE 'utc'::text")
    @Column(name = "created_at", nullable = false)
    var createdAt: Instant
)