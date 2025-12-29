package com.friendspark.backend.util

import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Component
import java.time.Clock
import java.time.Instant

@Component
@RequiredArgsConstructor
class DateTimeUtil(private val clock: Clock) {

    fun now(): Instant {
        return this.clock.instant()
    }
}