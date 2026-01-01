package com.friendspark.backend.service

import com.friendspark.backend.dto.interest.InterestCreateRequest
import com.friendspark.backend.dto.interest.InterestResponse
import com.friendspark.backend.dto.interest.InterestUpdateRequest
import com.friendspark.backend.entity.Interest
import com.friendspark.backend.mapper.InterestMapper
import com.friendspark.backend.repository.InterestRepository
import com.friendspark.backend.util.DateTimeUtil
import mu.KotlinLogging
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class InterestService(
    private val interestRepository: InterestRepository,
    private val interestMapper: InterestMapper,
    private val dateTimeUtil: DateTimeUtil,
) {
    private val logger = KotlinLogging.logger {}

    fun list(): List<InterestResponse> =
        interestRepository.findAll()
            .sortedBy { it.name.lowercase() }
            .map(interestMapper::toResponse)

    fun get(id: UUID): InterestResponse? =
        interestRepository.findById(id).orElse(null)?.let(interestMapper::toResponse)

    @Transactional
    fun create(req: InterestCreateRequest): InterestResponse {
        val normalizedName = normalizeName(req.name)
        logger.info { "Creating interest name='$normalizedName'" }

        if (interestRepository.existsByNameIgnoreCase(normalizedName)) {
            throw IllegalStateException("Interest with name '$normalizedName' already exists")
        }

        val entity = interestMapper.toEntity(req).apply {
            name = normalizedName
            createdAt = dateTimeUtil.now()
            updatedAt = createdAt
        }

        return try {
            interestMapper.toResponse(interestRepository.save(entity))
        } catch (_: DataIntegrityViolationException) {
            // In case of race condition with unique constraint
            throw IllegalStateException("Interest with name '$normalizedName' already exists")
        }
    }

    @Transactional
    fun update(id: UUID, patch: InterestUpdateRequest): InterestResponse {
        val existing = interestRepository.findById(id).orElseThrow { NoSuchElementException("Interest not found") }

        patch.name?.let {
            val normalizedName = normalizeName(it)
            val other = interestRepository.findByNameIgnoreCase(normalizedName)
            if (other != null && other.id != existing.id) {
                throw IllegalStateException("Interest with name '$normalizedName' already exists")
            }
            existing.name = normalizedName
        }

        interestMapper.update(existing, patch)
        existing.updatedAt = dateTimeUtil.now()

        return interestMapper.toResponse(interestRepository.save(existing))
    }

    @Transactional
    fun delete(id: UUID) {
        if (!interestRepository.existsById(id)) {
            throw NoSuchElementException("Interest not found")
        }
        interestRepository.deleteById(id)
    }

    /**
     * Resolve interest IDs to entities. Used by profile update.
     */
    fun resolveByIds(ids: Set<UUID>): Set<Interest> {
        if (ids.isEmpty()) return emptySet()
        val found = interestRepository.findAllById(ids).toList()
        val foundIds = found.mapNotNull { it.id }.toSet()
        val missing = ids - foundIds
        if (missing.isNotEmpty()) {
            throw IllegalArgumentException("Unknown interests: ${missing.joinToString(",")}")
        }
        return found.toSet()
    }

    private fun normalizeName(raw: String): String =
        raw.trim().replace(Regex("\\s+"), " ")
}
