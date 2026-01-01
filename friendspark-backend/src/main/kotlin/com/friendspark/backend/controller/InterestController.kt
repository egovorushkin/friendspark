package com.friendspark.backend.controller

import com.friendspark.backend.dto.interest.InterestCreateRequest
import com.friendspark.backend.dto.interest.InterestResponse
import com.friendspark.backend.dto.interest.InterestUpdateRequest
import com.friendspark.backend.service.InterestService
import jakarta.validation.Valid
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/interests")
class InterestController(
    private val interestService: InterestService,
) {
    private val logger = KotlinLogging.logger {}

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    fun list(): ResponseEntity<List<InterestResponse>> {
        val items = interestService.list()
        return ResponseEntity.ok(items)
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    fun get(@PathVariable id: UUID): ResponseEntity<InterestResponse> {
        val item = interestService.get(id) ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        return ResponseEntity.ok(item)
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    fun create(@Valid @RequestBody req: InterestCreateRequest): ResponseEntity<InterestResponse> {
        return try {
            val created = interestService.create(req)
            ResponseEntity.status(HttpStatus.CREATED).body(created)
        } catch (e: IllegalStateException) {
            logger.warn(e) { "Create interest conflict" }
            ResponseEntity.status(HttpStatus.CONFLICT).build()
        }
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    fun update(
        @PathVariable id: UUID,
        @Valid @RequestBody patch: InterestUpdateRequest,
    ): ResponseEntity<InterestResponse> {
        return try {
            ResponseEntity.ok(interestService.update(id, patch))
        } catch (e: NoSuchElementException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        } catch (e: IllegalStateException) {
            ResponseEntity.status(HttpStatus.CONFLICT).build()
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    fun delete(@PathVariable id: UUID): ResponseEntity<Unit> {
        return try {
            interestService.delete(id)
            ResponseEntity.noContent().build()
        } catch (e: NoSuchElementException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }
}

