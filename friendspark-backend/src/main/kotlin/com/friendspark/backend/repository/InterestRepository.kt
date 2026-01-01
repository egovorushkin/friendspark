package com.friendspark.backend.repository

import com.friendspark.backend.entity.Interest
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface InterestRepository : JpaRepository<Interest, UUID> {
    fun findByNameIgnoreCase(name: String): Interest?
    fun existsByNameIgnoreCase(name: String): Boolean
}
