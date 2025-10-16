package com.smartschedule.data.mappers

import com.smartschedule.data.local.entities.StandingAssignmentEntity
import com.smartschedule.domain.models.ShiftType
import com.smartschedule.domain.models.StandingAssignment
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.DayOfWeek

class StandingAssignmentMapperKtTest {

    private val testEntity = StandingAssignmentEntity(
        id = 1L,
        employeeId = 10L,
        dayOfWeek = DayOfWeek.MONDAY,
        shiftType = ShiftType.MORNING,
        active = true
    )

    private val testDomain = StandingAssignment(
        id = 2L,
        employeeId = 20L,
        dayOfWeek = DayOfWeek.TUESDAY,
        shiftType = ShiftType.NIGHT,
        active = false
    )

    @Test
    fun `toDomain mapping correctness`() {
        val domain = testEntity.toDomain()

        assertEquals(testEntity.id, domain.id)
        assertEquals(testEntity.employeeId, domain.employeeId)
        assertEquals(testEntity.dayOfWeek, domain.dayOfWeek)
        assertEquals(testEntity.shiftType, domain.shiftType)
        assertEquals(testEntity.active, domain.active)
    }

    @Test
    fun `toDomain  active  field is true`() {
        val entity = testEntity.copy(active = true)
        val domain = entity.toDomain()
        assertTrue(domain.active)
    }

    @Test
    fun `toDomain  active  field is false`() {
        val entity = testEntity.copy(active = false)
        val domain = entity.toDomain()
        assertFalse(domain.active)
    }

    @Test
    fun `toDomain with potentially nullable or default values`() {
        val defaultEntity = StandingAssignmentEntity(id = 0, employeeId = 0, dayOfWeek = DayOfWeek.SUNDAY, shiftType = ShiftType.MORNING, active = false)
        val domain = defaultEntity.toDomain()

        assertEquals(0L, domain.id)
        assertEquals(0L, domain.employeeId)
        assertEquals(DayOfWeek.SUNDAY, domain.dayOfWeek)
        assertEquals(ShiftType.MORNING, domain.shiftType)
        assertFalse(domain.active)
    }

    @Test
    fun `toEntity mapping correctness`() {
        val entity = testDomain.toEntity()

        assertEquals(testDomain.id, entity.id)
        assertEquals(testDomain.employeeId, entity.employeeId)
        assertEquals(testDomain.dayOfWeek, entity.dayOfWeek)
        assertEquals(testDomain.shiftType, entity.shiftType)
        assertEquals(testDomain.active, entity.active)
    }

    @Test
    fun `toEntity  active  field is true`() {
        val domain = testDomain.copy(active = true)
        val entity = domain.toEntity()
        assertTrue(entity.active)
    }

    @Test
    fun `toEntity  active  field is false`() {
        val domain = testDomain.copy(active = false)
        val entity = domain.toEntity()
        assertFalse(entity.active)
    }

    @Test
    fun `toEntity with potentially nullable or default values`() {
        val defaultDomain = StandingAssignment(id = 0, employeeId = 0, dayOfWeek = DayOfWeek.SATURDAY, shiftType = ShiftType.NIGHT, active = true)
        val entity = defaultDomain.toEntity()

        assertEquals(0L, entity.id)
        assertEquals(0L, entity.employeeId)
        assertEquals(DayOfWeek.SATURDAY, entity.dayOfWeek)
        assertEquals(ShiftType.NIGHT, entity.shiftType)
        assertTrue(entity.active)
    }

    @Test
    fun `Symmetry check  toDomain then toEntity`() {
        val roundTripEntity = testEntity.toDomain().toEntity()
        assertEquals(testEntity, roundTripEntity)
    }

    @Test
    fun `Symmetry check  toEntity then toDomain`() {
        val roundTripDomain = testDomain.toEntity().toDomain()
        assertEquals(testDomain, roundTripDomain)
    }

    @Test
    fun `Mapping with large list of entities`() {
        val entityList = (1..1000).map {
            testEntity.copy(id = it.toLong())
        }
        val domainList = entityList.map { it.toDomain() }

        assertEquals(1000, domainList.size)
        assertEquals(500L, domainList[499].id)
    }

    @Test
    fun `Mapping with large list of domain models`() {
        val domainList = (1..1000).map {
            testDomain.copy(id = it.toLong())
        }
        val entityList = domainList.map { it.toEntity() }

        assertEquals(1000, entityList.size)
        assertEquals(500L, entityList[499].id)
    }

}
