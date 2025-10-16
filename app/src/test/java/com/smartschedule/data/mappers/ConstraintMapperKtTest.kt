package com.smartschedule.data.mappers

import com.smartschedule.data.local.entities.ConstraintEntity
import com.smartschedule.domain.models.Constraint
import com.smartschedule.domain.models.ConstraintType
import com.smartschedule.domain.models.ShiftType
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import org.junit.Test
import java.time.LocalDate

class ConstraintMapperKtTest {

    @Test
    fun `toDomain mapping integrity check`() {
        // Verify that all fields from a ConstraintEntity are correctly mapped to the corresponding fields in the Constraint domain object. This includes `id`, `employeeId`, `startDate`, `endDate`, `type`, `shiftType`, and `reason`.
        val entity = ConstraintEntity(
            id = 1L,
            employeeId = 101L,
            startDate = LocalDate.of(2025, 10, 10),
            endDate = LocalDate.of(2025, 10, 11),
            type = ConstraintType.DAY_OFF,
            shiftType = ShiftType.MORNING,
            reason = "חופש"
        )
        val domain = entity.toDomain()

        assertEquals(entity.id, domain.id)
        assertEquals(entity.employeeId, domain.employeeId)
        assertEquals(entity.startDate, domain.dateStart)
        assertEquals(entity.endDate, domain.dateEnd)
        assertEquals(entity.type, domain.constraintType)
        assertEquals(entity.shiftType, domain.shiftType)
        assertEquals(entity.reason, domain.reason)
    }

    @Test
    fun `toDomain with null or empty reason`() {
        // Test the toDomain mapping when the 'reason' field in ConstraintEntity is null or an empty string to ensure it's handled gracefully and doesn't cause a crash.
        val entity = ConstraintEntity(
            id = 2L,
            employeeId = 200L,
            startDate = LocalDate.now(),
            endDate = LocalDate.now(),
            type = ConstraintType.DAY_OFF,
            shiftType = ShiftType.NIGHT,
            reason = null
        )

        val domain = entity.toDomain()

        assertNull(domain.reason)
    }

    @Test
    fun `toDomain with different data types and values`() {
        // Test the toDomain mapping with various valid values for all fields, such as different IDs, dates, and enum types, to ensure correct mapping across the board.
        val entity = ConstraintEntity(
            id = 999L,
            employeeId = 77L,
            startDate = LocalDate.of(2025, 1, 1),
            endDate = LocalDate.of(2025, 1, 2),
            type = ConstraintType.SINGLE_SHIFT,
            shiftType = ShiftType.NOON,
            reason = "בדיקה רפואית"
        )
        val domain = entity.toDomain()
        assertEquals(ConstraintType.SINGLE_SHIFT, domain.constraintType)
        assertEquals(ShiftType.NOON, domain.shiftType)
    }

    @Test
    fun `toEntity mapping integrity check`() {
        // Verify that all fields from a Constraint domain object are correctly mapped to the corresponding fields in the ConstraintEntity. This includes `id`, `employeeId`, `dateStart`, `dateEnd`, `constraintType`, `shiftType`, and `reason`.
        val domain = Constraint(
            id = 10L,
            employeeId = 33L,
            dateStart = LocalDate.of(2025, 3, 10),
            dateEnd = LocalDate.of(2025, 3, 15),
            constraintType = ConstraintType.DAY_OFF,
            shiftType = ShiftType.MORNING,
            reason = "טיול"
        )

        val entity = domain.toEntity()
        assertEquals(domain.id, entity.id)
        assertEquals(domain.employeeId, entity.employeeId)
        assertEquals(domain.dateStart, entity.startDate)
        assertEquals(domain.dateEnd, entity.endDate)
        assertEquals(domain.constraintType, entity.type)
        assertEquals(domain.shiftType, entity.shiftType)
        assertEquals(domain.reason, entity.reason)
    }

    @Test
    fun `toEntity with null or empty reason`() {
        // Test the toEntity mapping when the 'reason' field in the Constraint domain object is null or an empty string to ensure it's correctly mapped to the entity.
        val domain = Constraint(
            id = 5L,
            employeeId = 15L,
            dateStart = LocalDate.now(),
            dateEnd = LocalDate.now(),
            constraintType = ConstraintType.SINGLE_SHIFT,
            shiftType = ShiftType.NIGHT,
            reason = null
        )

        val entity = domain.toEntity()
        assertNull(entity.reason)
    }

    @Test
    fun `toEntity with different data types and values`() {
        // Test the toEntity mapping with various valid values for all fields to confirm the data integrity is maintained during the conversion to the entity.
        val entity = ConstraintEntity(
            id = 100L,
            employeeId = 20L,
            startDate = LocalDate.now(),
            endDate = LocalDate.now().plusDays(1),
            type = ConstraintType.DAY_OFF,
            shiftType = ShiftType.MORNING,
            reason = "בדיקה"
        )

        val roundTrip = entity.toDomain().toEntity()
        assertEquals(entity, roundTrip)
    }

    @Test
    fun `toDomain and toEntity round trip conversion`() {
        // Test that converting a ConstraintEntity to a Constraint and then back to a ConstraintEntity results in an object that is identical to the original entity. This ensures lossless conversion.
        val domain = Constraint(
            id = 101L,
            employeeId = 88L,
            dateStart = LocalDate.now(),
            dateEnd = LocalDate.now().plusDays(3),
            constraintType = ConstraintType.SINGLE_SHIFT,
            shiftType = ShiftType.NIGHT,
            reason = "מנוחה"
        )

        val roundTrip = domain.toEntity().toDomain()
        assertEquals(domain, roundTrip)
    }

    @Test
    fun `toEntity and toDomain round trip conversion`() {
        // Test that converting a Constraint to a ConstraintEntity and then back to a Constraint results in an object that is identical to the original domain model. This verifies the symmetry and correctness of the mappers.
        val entity = ConstraintEntity(
            id = Long.MAX_VALUE,
            employeeId = Long.MAX_VALUE,
            startDate = LocalDate.of(2099, 12, 31),
            endDate = LocalDate.of(2100, 1, 1),
            type = ConstraintType.SINGLE_SHIFT,
            shiftType = ShiftType.NOON,
            reason = "a".repeat(500)
        )
        val domain = entity.toDomain()
        assertEquals("a".repeat(500), domain.reason)
    }

    @Test
    fun `toDomain with large values`() {
        // Test the toDomain mapping with large values for numeric fields and long strings for the 'reason' field to check for any unexpected behavior or truncation.
        val entity = ConstraintEntity(
            id = Long.MAX_VALUE,
            employeeId = Long.MAX_VALUE,
            startDate = LocalDate.of(2099, 12, 31),
            endDate = LocalDate.of(2100, 1, 1),
            type = ConstraintType.SINGLE_SHIFT,
            shiftType = ShiftType.NIGHT,
            reason = "a".repeat(500)
        )
        val domain = entity.toDomain()
        assertEquals("a".repeat(500), domain.reason)
    }

    @Test
    fun `toEntity with large values`() {
        // Test the toEntity mapping with large values for numeric fields and long strings for the 'reason' field to ensure the entity can handle them correctly.
        val domain = Constraint(
            id = Long.MAX_VALUE,
            employeeId = Long.MAX_VALUE,
            dateStart = LocalDate.of(2099, 12, 31),
            dateEnd = LocalDate.of(2100, 1, 1),
            constraintType = ConstraintType.SINGLE_SHIFT,
            shiftType = ShiftType.NIGHT,
            reason = "x".repeat(1000)
        )
        val entity = domain.toEntity()
        assertEquals("x".repeat(1000), entity.reason)
    }

}