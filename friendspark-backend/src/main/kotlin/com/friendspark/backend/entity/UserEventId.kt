package com.friendspark.backend.entity

import lombok.EqualsAndHashCode
import java.util.*

@EqualsAndHashCode
class UserEventId(
    var user: UUID,
    var event: UUID
) : java.io.Serializable