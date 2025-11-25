package com.friendspark.backend.repository

import com.friendspark.backend.entity.Event
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface EventRepository : JpaRepository<Event, UUID>
