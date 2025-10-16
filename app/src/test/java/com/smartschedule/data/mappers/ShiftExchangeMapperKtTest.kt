package com.smartschedule.data.mappers

import com.smartschedule.data.local.entities.ShiftExchangeEntity
import com.smartschedule.domain.models.ExchangeStatus
import com.smartschedule.domain.models.ShiftExchange
import org.junit.Assert.*
import org.junit.Test
import java.time.LocalDateTime

class ShiftExchangeMapperKtTest {

    private val testEntity = ShiftExchangeEntity(
        id = 1L,
        fromEmployeeId = 10L,
        toEmployeeId = 20L,
        shiftId = 100L,
        status = ExchangeStatus.APPROVED,
        requestedAt = LocalDateTime.of(2024, 1, 1, 10, 0),
        resolvedAt = LocalDateTime.of(2024, 1, 1, 12, 0),
        managerNote = "Approved by manager"
    )

    private val testDomain = ShiftExchange(
        id = 2L,
        fromEmployeeId = 11L,
        toEmployeeId = 21L,
        shiftId = 101L,
        status = ExchangeStatus.PENDING,
        requestedAt = LocalDateTime.of(2024, 2, 1, 10, 0),
        resolvedAt = null,
        managerNote = "Waiting for approval"
    )

    @Test
    fun `toDomain mapping correctness`() {
        val domain = testEntity.toDomain()

        assertEquals(testEntity.id, domain.id)
        assertEquals(testEntity.fromEmployeeId, domain.fromEmployeeId)
        assertEquals(testEntity.toEmployeeId, domain.toEmployeeId)
        assertEquals(testEntity.shiftId, domain.shiftId)
        assertEquals(testEntity.status, domain.status)
        assertEquals(testEntity.requestedAt, domain.requestedAt)
        assertEquals(testEntity.resolvedAt, domain.resolvedAt)
        assertEquals(testEntity.managerNote, domain.managerNote)
    }

    @Test
    fun `toDomain with nullable  toEmployeeId `() {
        val entity = testEntity.copy(toEmployeeId = null)
        val domain = entity.toDomain()
        assertNull(domain.toEmployeeId)
    }

    @Test
    fun `toDomain with nullable  resolvedAt `() {
        val entity = testEntity.copy(resolvedAt = null)
        val domain = entity.toDomain()
        assertNull(domain.resolvedAt)
    }

    @Test
    fun `toDomain with nullable  managerNote `() {
        val entity = testEntity.copy(managerNote = null)
        val domain = entity.toDomain()
        assertNull(domain.managerNote)
    }

    @Test
    fun `toDomain with empty  managerNote `() {
        val entity = testEntity.copy(managerNote = "")
        val domain = entity.toDomain()
        assertEquals("", domain.managerNote)
    }

    @Test
    fun `toDomain with all nullable fields null`() {
        val entity = testEntity.copy(toEmployeeId = null, resolvedAt = null, managerNote = null)
        val domain = entity.toDomain()

        assertNull(domain.toEmployeeId)
        assertNull(domain.resolvedAt)
        assertNull(domain.managerNote)
    }

    @Test
    fun `toEntity mapping correctness`() {
        val entity = testDomain.toEntity()

        assertEquals(testDomain.id, entity.id)
        assertEquals(testDomain.fromEmployeeId, entity.fromEmployeeId)
        assertEquals(testDomain.toEmployeeId, entity.toEmployeeId)
        assertEquals(testDomain.shiftId, entity.shiftId)
        assertEquals(testDomain.status, entity.status)
        assertEquals(testDomain.requestedAt, entity.requestedAt)
        assertEquals(testDomain.resolvedAt, entity.resolvedAt)
        assertEquals(testDomain.managerNote, entity.managerNote)
    }

    @Test
    fun `toEntity with nullable  toEmployeeId `() {
        val domain = testDomain.copy(toEmployeeId = null)
        val entity = domain.toEntity()
        assertNull(entity.toEmployeeId)
    }

    @Test
    fun `toEntity with nullable  resolvedAt `() {
        val domain = testDomain.copy(resolvedAt = null)
        val entity = domain.toEntity()
        assertNull(entity.resolvedAt)
    }

    @Test
    fun `toEntity with nullable  managerNote `() {
        val domain = testDomain.copy(managerNote = null)
        val entity = domain.toEntity()
        assertNull(entity.managerNote)
    }

    @Test
    fun `toEntity with empty  managerNote `() {
        val domain = testDomain.copy(managerNote = "")
        val entity = domain.toEntity()
        assertEquals("", entity.managerNote)
    }

    @Test
    fun `toEntity with all nullable fields null`() {
        val domain = testDomain.copy(toEmployeeId = null, resolvedAt = null, managerNote = null)
        val entity = domain.toEntity()

        assertNull(entity.toEmployeeId)
        assertNull(entity.resolvedAt)
        assertNull(entity.managerNote)
    }

    @Test
    fun `toDomain and toEntity round trip`() {
        val roundTripEntity = testEntity.toDomain().toEntity()
        assertEquals(testEntity, roundTripEntity)
    }

    @Test
    fun `toEntity and toDomain round trip`() {
        val roundTripDomain = testDomain.toEntity().toDomain()
        assertEquals(testDomain, roundTripDomain)
    }
}
