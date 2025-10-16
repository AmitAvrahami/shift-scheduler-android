package com.smartschedule.data.mappers

import com.smartschedule.data.local.entities.WorkScheduleEntity
import com.smartschedule.domain.models.ScheduleStatus
import com.smartschedule.domain.models.WorkSchedule
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime

class WorkScheduleMapperKtTest {

    private val testEntity = WorkScheduleEntity(
        id = 1L,
        weekStartDate = LocalDate.of(2024, 1, 7), // A Sunday
        status = ScheduleStatus.PUBLISHED,
        createdBy = "test_user_entity",
        createdAt = LocalDateTime.of(2024, 1, 1, 10, 0),
        approvedAt = LocalDateTime.of(2024, 1, 2, 10, 0)
    )

    private val testDomain = WorkSchedule(
        id = 2L,
        weekStartDate = LocalDate.of(2024, 1, 14), // A Sunday
        status = ScheduleStatus.DRAFT,
        createdBy = "test_user_domain",
        createdAt = LocalDateTime.of(2024, 1, 8, 11, 0),
        approvedAt = null
    )

    @Test
    fun `toDomain happy path mapping`() {
        val domain = testEntity.toDomain()

        assertEquals(testEntity.id, domain.id)
        assertEquals(testEntity.weekStartDate, domain.weekStartDate)
        assertEquals(testEntity.status, domain.status)
        assertEquals(testEntity.createdBy, domain.createdBy)
        assertEquals(testEntity.createdAt, domain.createdAt)
        assertEquals(testEntity.approvedAt, domain.approvedAt)
    }

    @Test
    fun `toEntity happy path mapping`() {
        val entity = testDomain.toEntity()

        assertEquals(testDomain.id, entity.id)
        assertEquals(testDomain.weekStartDate, entity.weekStartDate)
        assertEquals(testDomain.status, entity.status)
        assertEquals(testDomain.createdBy, entity.createdBy)
        assertEquals(testDomain.createdAt, entity.createdAt)
        assertEquals(testDomain.approvedAt, entity.approvedAt)
    }

    @Test
    fun `toDomain with null approvedAt`() {
        val entityWithNull = testEntity.copy(approvedAt = null)
        val domain = entityWithNull.toDomain()
        assertNull(domain.approvedAt)
    }

    @Test
    fun `toEntity with null approvedAt`() {
        val domainWithNull = testDomain.copy(approvedAt = null)
        val entity = domainWithNull.toEntity()
        assertNull(entity.approvedAt)
    }

    @Test
    fun `toDomain data type consistency`() {
        val domain = testEntity.toDomain()
        assertEquals(testEntity.id, domain.id)
        assertEquals(testEntity.weekStartDate, domain.weekStartDate)
        assertEquals(testEntity.status, domain.status)
        assertEquals(testEntity.createdBy, domain.createdBy)
        assertEquals(testEntity.createdAt, domain.createdAt)
        assertEquals(testEntity.approvedAt, domain.approvedAt)
    }

    @Test
    fun `toEntity data type consistency`() {
        val entity = testDomain.toEntity()
        assertEquals(testDomain.id, entity.id)
        assertEquals(testDomain.weekStartDate, entity.weekStartDate)
        assertEquals(testDomain.status, entity.status)
        assertEquals(testDomain.createdBy, entity.createdBy)
        assertEquals(testDomain.createdAt, entity.createdAt)
        assertEquals(testDomain.approvedAt, entity.approvedAt)
    }

    @Test
    fun `toDomain mapping integrity check`() {
        val roundTripEntity = testEntity.toDomain().toEntity()
        assertEquals(testEntity, roundTripEntity)
    }

    @Test
    fun `toEntity mapping integrity check`() {
        val roundTripDomain = testDomain.toEntity().toDomain()
        assertEquals(testDomain, roundTripDomain)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `toDomain with empty createdBy`() {
        val entity = testEntity.copy(createdBy = "")
        entity.toDomain() // Should throw due to domain model validation
    }

    @Test(expected = IllegalArgumentException::class)
    fun `toEntity with empty createdBy`() {
        val domain = testDomain.copy(createdBy = "")
        val entity = domain.toEntity()
        assertEquals("", entity.createdBy)
    }

    @Test
    fun `toDomain with special characters in strings`() {
        val specialCreator = "Jøhn Døe !@#$%^&*()"
        val entity = testEntity.copy(createdBy = specialCreator)
        val domain = entity.toDomain()
        assertEquals(specialCreator, domain.createdBy)
    }

    @Test
    fun `toEntity with special characters in strings`() {
        val specialCreator = "Jøhn Døe !@#$%^&*()"
        val domain = testDomain.copy(createdBy = specialCreator)
        val entity = domain.toEntity()
        assertEquals(specialCreator, entity.createdBy)
    }

    @Test
    fun `toDomain with min max date values`() {
        val minSunday = LocalDate.MIN.with(DayOfWeek.SUNDAY)
        val entity = testEntity.copy(
            weekStartDate = minSunday,
            createdAt = LocalDateTime.MIN,
            approvedAt = LocalDateTime.MAX
        )
        val domain = entity.toDomain()
        assertEquals(minSunday, domain.weekStartDate)
        assertEquals(LocalDateTime.MIN, domain.createdAt)
        assertEquals(LocalDateTime.MAX, domain.approvedAt)
    }

    @Test
    fun `toEntity with min max date values`() {
        val minSunday = LocalDate.MIN.with(DayOfWeek.SUNDAY)
        val domain = testDomain.copy(
            weekStartDate = minSunday,
            createdAt = LocalDateTime.MIN,
            approvedAt = LocalDateTime.MAX
        )
        val entity = domain.toEntity()
        assertEquals(minSunday, entity.weekStartDate)
        assertEquals(LocalDateTime.MIN, entity.createdAt)
        assertEquals(LocalDateTime.MAX, entity.approvedAt)
    }
}
