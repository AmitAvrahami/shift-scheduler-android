package com.smartschedule.data.mappers

import com.smartschedule.data.local.entities.RecurringConstraintEntity
import com.smartschedule.domain.models.RecurringConstraint
import com.smartschedule.domain.models.ShiftType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.time.DayOfWeek
import java.time.LocalDate

class RecurringConstraintMapperKtTest {

    private val recurringConstraintEntity = RecurringConstraintEntity(
        id = 1L,
        employeeId = 10L,
        dayOfWeek = DayOfWeek.MONDAY,
        shiftTypes = setOf(ShiftType.MORNING, ShiftType.NIGHT),
        reason = "Test Reason",
        validFrom = LocalDate.of(2024, 1, 1),
        validUntil = LocalDate.of(2024, 12, 31)
    )

    private val recurringConstraintDomain = RecurringConstraint(
        id = 2L,
        employeeId = 20L,
        dayOfWeek = DayOfWeek.TUESDAY,
        shiftTypes = setOf(ShiftType.NOON),
        reason = "Domain Test Reason",
        validFrom = LocalDate.of(2025, 1, 1),
        validUntil = LocalDate.of(2025, 12, 31)
    )

    @Test
    fun `toDomain  Basic successful mapping`() {
        val domain = recurringConstraintEntity.toDomain()

        assertEquals(recurringConstraintEntity.id, domain.id)
        assertEquals(recurringConstraintEntity.employeeId, domain.employeeId)
        assertEquals(recurringConstraintEntity.dayOfWeek, domain.dayOfWeek)
        assertEquals(recurringConstraintEntity.shiftTypes, domain.shiftTypes)
        assertEquals(recurringConstraintEntity.reason, domain.reason)
        assertEquals(recurringConstraintEntity.validFrom, domain.validFrom)
        assertEquals(recurringConstraintEntity.validUntil, domain.validUntil)
    }

    @Test
    fun `toDomain  Mapping with null reason`() {
        val entity = recurringConstraintEntity.copy(reason = null)
        val domain = entity.toDomain()
        assertNull(domain.reason)
    }

    @Test
    fun `toDomain  Mapping with null validUntil`() {
        val entity = recurringConstraintEntity.copy(validUntil = null)
        val domain = entity.toDomain()
        assertNull(domain.validUntil)
    }

    @Test
    fun `toDomain  Mapping with empty shiftTypes list`() {
        val entity = recurringConstraintEntity.copy(shiftTypes = emptySet())
        val domain = entity.toDomain()
        assertEquals(emptySet<ShiftType>(), domain.shiftTypes)
    }

    @Test
    fun `toDomain  Mapping with all nullable fields as null`() {
        val entity = recurringConstraintEntity.copy(reason = null, validFrom = null, validUntil = null)
        val domain = entity.toDomain()
        assertNull(domain.reason)
        assertNull(domain.validFrom)
        assertNull(domain.validUntil)
    }

    @Test
    fun `toDomain  Mapping with special characters in string fields`() {
        val specialReason = "Reason with !@#$%^&*() and unicode: ö, ä, ü, ß"
        val entity = recurringConstraintEntity.copy(reason = specialReason)
        val domain = entity.toDomain()
        assertEquals(specialReason, domain.reason)
    }

    @Test
    fun `toDomain  Mapping with extreme date values`() {
        val entity = recurringConstraintEntity.copy(validFrom = LocalDate.MIN, validUntil = LocalDate.MAX)
        val domain = entity.toDomain()
        assertEquals(LocalDate.MIN, domain.validFrom)
        assertEquals(LocalDate.MAX, domain.validUntil)
    }

    @Test
    fun `toEntity  Basic successful mapping`() {
        val entity = recurringConstraintDomain.toEntity()

        assertEquals(recurringConstraintDomain.id, entity.id)
        assertEquals(recurringConstraintDomain.employeeId, entity.employeeId)
        assertEquals(recurringConstraintDomain.dayOfWeek, entity.dayOfWeek)
        assertEquals(recurringConstraintDomain.shiftTypes, entity.shiftTypes)
        assertEquals(recurringConstraintDomain.reason, entity.reason)
        assertEquals(recurringConstraintDomain.validFrom, entity.validFrom)
        assertEquals(recurringConstraintDomain.validUntil, entity.validUntil)
    }

    @Test
    fun `toEntity  Mapping with null reason`() {
        val domain = recurringConstraintDomain.copy(reason = null)
        val entity = domain.toEntity()
        assertNull(entity.reason)
    }

    @Test
    fun `toEntity  Mapping with null validUntil`() {
        val domain = recurringConstraintDomain.copy(validUntil = null)
        val entity = domain.toEntity()
        assertNull(entity.validUntil)
    }

    @Test
    fun `toEntity  Mapping with empty shiftTypes list`() {
        val domain = RecurringConstraint(
            id = 3L, employeeId = 30L, dayOfWeek = DayOfWeek.FRIDAY, shiftTypes = emptySet()
        )
        val entity = domain.toEntity()
        assertEquals(emptySet<ShiftType>(), entity.shiftTypes)
    }

    @Test
    fun `toEntity  Mapping with all nullable fields as null`() {
        val domain = recurringConstraintDomain.copy(reason = null, validFrom = null, validUntil = null)
        val entity = domain.toEntity()
        assertNull(entity.reason)
        assertNull(entity.validFrom)
        assertNull(entity.validUntil)
    }

    @Test
    fun `toEntity  Mapping with special characters in string fields`() {
        val specialReason = "Reason with !@#$%^&*() and unicode: ö, ä, ü, ß"
        val domain = recurringConstraintDomain.copy(reason = specialReason)
        val entity = domain.toEntity()
        assertEquals(specialReason, entity.reason)
    }

    @Test
    fun `toEntity  Mapping with extreme date values`() {
        val domain = recurringConstraintDomain.copy(validFrom = LocalDate.MIN, validUntil = LocalDate.MAX)
        val entity = domain.toEntity()
        assertEquals(LocalDate.MIN, entity.validFrom)
        assertEquals(LocalDate.MAX, entity.validUntil)
    }

    @Test
    fun `Symmetry check  toDomain followed by toEntity`() {
        val roundTripEntity = recurringConstraintEntity.toDomain().toEntity()
        assertEquals(recurringConstraintEntity, roundTripEntity)
    }

    @Test
    fun `Symmetry check  toEntity followed by toDomain`() {
        val roundTripDomain = recurringConstraintDomain.toEntity().toDomain()
        assertEquals(recurringConstraintDomain, roundTripDomain)
    }
}
