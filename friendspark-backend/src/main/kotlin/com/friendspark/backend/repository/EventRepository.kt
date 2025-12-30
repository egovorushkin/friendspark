package com.friendspark.backend.repository

import com.friendspark.backend.entity.Event
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

@Repository
interface EventRepository : JpaRepository<Event, UUID> {
    @Query("SELECT e FROM Event e JOIN FETCH e.creator WHERE e.id = :id")
    override fun findById(id: UUID): Optional<Event>
}
