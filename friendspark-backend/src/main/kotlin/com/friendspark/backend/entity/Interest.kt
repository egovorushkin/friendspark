package com.friendspark.backend.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import lombok.Getter
import lombok.Setter
import java.time.Instant
import java.util.UUID

@Entity
@Getter
@Setter
@Table(
    schema = "friendspark",
    name = "interests"
)
class Interest(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    @Column(nullable = false, unique = true, length = 50)
    var name: String = "",

    @Column(name = "icon_name", length = 50)
    var iconName: String? = null,

    @Column(name = "created_at", nullable = false)
    var createdAt: Instant = Instant.EPOCH,

    @Column(name = "updated_at", nullable = false)
    var updatedAt: Instant = Instant.EPOCH,
)