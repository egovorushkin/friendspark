package com.friendspark.backend.entity

import java.io.Serializable
import java.util.*

/**
 * Composite key for UserEvent entity.
 */
class UserEventId(
    var user: UUID? = null,
    var event: UUID? = null
) : Serializable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        other as UserEventId
        return user == other.user && event == other.event
    }
    override fun hashCode(): Int = Objects.hash(user, event)
}

