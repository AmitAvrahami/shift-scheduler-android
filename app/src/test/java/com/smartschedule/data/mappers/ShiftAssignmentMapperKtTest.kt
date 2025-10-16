package com.smartschedule.data.mappers

import com.smartschedule.data.local.entities.ShiftAssignmentEntity
import com.smartschedule.domain.models.ShiftAssignment
import com.smartschedule.domain.models.ShiftType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test
import java.time.LocalDateTime

class ShiftAssignmentMapperKtTest {
    private val shiftAssignmentDomain = ShiftAssignment(
        id = 1L,
        shiftId = 2L,
        employeeId = 3L,
        assignedAt = LocalDateTime.of(2025, 10, 14, 14, 30),
        assignedBy = "amitDomain",
        shiftType = ShiftType.NIGHT
    )
    private val shiftAssignmentEntity = ShiftAssignmentEntity(
        id = 4L,
        shiftId = 5L,
        employeeId = 6L,
        assignedAt = LocalDateTime.of(2025, 10, 15, 14, 30),
        assignedBy = "amitEntity"
    )

    @Test
    fun `toDomain mapping correctness`() {
        val domainObject = shiftAssignmentEntity.toDomain()

        assertEquals(shiftAssignmentEntity.id, domainObject.id)
        assertEquals(shiftAssignmentEntity.shiftId, domainObject.shiftId)
        assertEquals(shiftAssignmentEntity.employeeId, domainObject.employeeId)
        assertEquals(shiftAssignmentEntity.assignedAt, domainObject.assignedAt)
        assertEquals(shiftAssignmentEntity.assignedBy, domainObject.assignedBy)
    }

    @Test
    fun `toDomain with max value fields`() {
        val maxEntity = ShiftAssignmentEntity(
            id = Long.MAX_VALUE,
            shiftId = Long.MAX_VALUE,
            employeeId = Long.MAX_VALUE,
            assignedAt = LocalDateTime.MAX,
            assignedBy = "a".repeat(255) // Assuming a max length
        )
        val domainObject = maxEntity.toDomain()

        assertEquals(Long.MAX_VALUE, domainObject.id)
        assertEquals(Long.MAX_VALUE, domainObject.shiftId)
        assertEquals(Long.MAX_VALUE, domainObject.employeeId)
        assertEquals(LocalDateTime.MAX, domainObject.assignedAt)
        assertEquals("a".repeat(255), domainObject.assignedBy)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `toDomain with empty or default entity`() {
        val defaultEntity = ShiftAssignmentEntity(id = 0, shiftId = 0, employeeId = 0, assignedAt = LocalDateTime.MIN, assignedBy = "")
        val domainObject = defaultEntity.toDomain()

        assertEquals(0L, domainObject.id)
        assertEquals(0L, domainObject.shiftId)
        assertEquals(0L, domainObject.employeeId)
        assertEquals(LocalDateTime.MIN, domainObject.assignedAt)
        assertEquals("", domainObject.assignedBy)
    }

    @Test
    fun `toEntity mapping correctness`() {
        val entityObject = shiftAssignmentDomain.toEntity()

        assertEquals(shiftAssignmentDomain.id, entityObject.id)
        assertEquals(shiftAssignmentDomain.shiftId, entityObject.shiftId)
        assertEquals(shiftAssignmentDomain.employeeId, entityObject.employeeId)
        assertEquals(shiftAssignmentDomain.assignedAt, entityObject.assignedAt)
        assertEquals(shiftAssignmentDomain.assignedBy, entityObject.assignedBy)
    }

    @Test
    fun `toEntity with max value fields`() {
        val maxDomain = ShiftAssignment(
            id = Long.MAX_VALUE,
            shiftId = Long.MAX_VALUE,
            employeeId = Long.MAX_VALUE,
            assignedAt = LocalDateTime.MAX,
            assignedBy = "b".repeat(255),
            shiftType = ShiftType.NIGHT
        )
        val entityObject = maxDomain.toEntity()

        assertEquals(Long.MAX_VALUE, entityObject.id)
        assertEquals(Long.MAX_VALUE, entityObject.shiftId)
        assertEquals(Long.MAX_VALUE, entityObject.employeeId)
        assertEquals(LocalDateTime.MAX, entityObject.assignedAt)
        assertEquals("b".repeat(255), entityObject.assignedBy)
    }

    @Test
    fun `toEntity throws exception for invalid domain`() {
        assertThrows(IllegalArgumentException::class.java) {
            ShiftAssignment(
                id = 0,
                shiftId = 0,
                employeeId = 0,
                assignedAt = LocalDateTime.MIN,
                assignedBy = "",
                shiftType = ShiftType.NIGHT
            ).toEntity()
        }
    }

    @Test
    fun `toDomain toEntity round trip`() {
        val roundTripEntity = shiftAssignmentEntity.toDomain().toEntity()
        assertEquals(shiftAssignmentEntity, roundTripEntity)
    }

    @Test
    fun `toEntity toDomain round trip`() {
        val roundTripDomain = shiftAssignmentDomain.toEntity().toDomain()
        assertEquals(roundTripDomain.id, shiftAssignmentDomain.id)
        assertEquals(roundTripDomain.shiftId, shiftAssignmentDomain.shiftId)
        assertEquals(roundTripDomain.employeeId, shiftAssignmentDomain.employeeId)
        assertEquals(roundTripDomain.assignedAt, shiftAssignmentDomain.assignedAt)
        assertEquals(roundTripDomain.assignedBy, shiftAssignmentDomain.assignedBy)
    }
}
