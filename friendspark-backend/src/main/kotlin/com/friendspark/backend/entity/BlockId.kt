package com.friendspark.backend.entity

import lombok.EqualsAndHashCode
import java.util.*

@EqualsAndHashCode
class BlockId(
    var blocker: UUID,
    var blocked: UUID
) : java.io.Serializable
