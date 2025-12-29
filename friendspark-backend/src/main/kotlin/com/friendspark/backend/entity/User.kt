package com.friendspark.backend.entity

import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import org.hibernate.annotations.ColumnDefault
import org.hibernate.annotations.JdbcType
import org.hibernate.dialect.type.PostgreSQLEnumJdbcType
import java.time.Instant
import java.time.LocalDate
import java.util.*

@Entity
@Table(
    schema = "friendspark",
    name = "users"
)
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    var id: UUID? = null,

    @Column(name = "firebase_uid", nullable = false, unique = true)
    val firebaseUid: String,

    @Column(nullable = false, unique = true, length = 255)
    val email: String,

    @Column(name = "first_name", nullable = false, length = 100)
    var firstName: String? = null,

    @Column(name = "last_name", nullable = false, length = 100)
    var lastName: String? = null,

    @Column(name = "photo_url")
    var photoUrl: String? = null,

    @Enumerated
    @JdbcType(PostgreSQLEnumJdbcType::class)
    @Column(nullable = false)
    var role: UserRole = UserRole.USER,

    @Column(name = "is_verified")
    var isVerified: Boolean = false,

    @Column(name = "is_banned")
    var isBanned: Boolean = false,

    var bannedReason: String? = null,

    @Column(name = "banned_by")
    var bannedBy: UUID? = null,

    @Column(name = "banned_at")
    var bannedAt: Instant? = null,

    @Column(length = 12)
    var geohash: String? = null,

    var latitude: Double? = null,
    var longitude: Double? = null,

    // Profile
    var birthDate: LocalDate? = null,

    var bio: String? = null,

    @Enumerated(EnumType.STRING)
    var gender: Gender? = null,

    @NotNull
    @ColumnDefault("(now) AT TIME ZONE 'utc'::text")
    @Column(name = "last_active_at", nullable = false)
    var lastActiveAt: Instant,

    // Relationships
    @OneToMany(mappedBy = "creator", cascade = [CascadeType.ALL], orphanRemoval = true)
    var createdEvents: MutableSet<Event>? = null,

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    var rsvps: MutableSet<UserEvent>? = null,

    @ManyToMany
    @JoinTable(
        name = "user_interests",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "interest_id")]
    )
    var interests: MutableSet<Interest>? = null,

    @NotNull
    @ColumnDefault("(now) AT TIME ZONE 'utc'::text")
    @Column(name = "created_at", nullable = false)
    var createdAt: Instant,

    @NotNull
    @ColumnDefault("(now) AT TIME ZONE 'utc'::text")
    @Column(name = "updated_at", nullable = false)
    var updatedAt: Instant,
)