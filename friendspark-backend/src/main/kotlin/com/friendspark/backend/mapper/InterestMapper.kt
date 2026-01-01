package com.friendspark.backend.mapper

import com.friendspark.backend.dto.interest.InterestCreateRequest
import com.friendspark.backend.dto.interest.InterestResponse
import com.friendspark.backend.dto.interest.InterestUpdateRequest
import com.friendspark.backend.entity.Interest
import org.springframework.stereotype.Component

@Component
class InterestMapper {
    fun toEntity(req: InterestCreateRequest): Interest =
        Interest(
            name = req.name.trim(),
            iconName = req.iconName?.trim(),
        )

    fun update(existing: Interest, patch: InterestUpdateRequest): Interest = existing.apply {
        patch.name?.let { name = it.trim() }
        patch.iconName?.let { iconName = it.trim() }
    }

    fun toResponse(entity: Interest): InterestResponse =
        InterestResponse(
            id = entity.id!!,
            name = entity.name,
            iconName = entity.iconName,
        )
}
