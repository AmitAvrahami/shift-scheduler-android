package com.smartschedule.data.mappers

import com.smartschedule.data.local.entities.ShiftTemplateEntity
import com.smartschedule.domain.models.ShiftTemplate
import com.smartschedule.domain.models.ShiftType
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.DayOfWeek

class ShiftTemplateMapperKtTest {

    private val testEntity = ShiftTemplateEntity(
        id = 1L,
        dayOfWeek = DayOfWeek.MONDAY,
        shiftType = ShiftType.MORNING,
        requiredHeadcount = 5
    )

    private val testDomain = ShiftTemplate(
        id = 2L,
        dayOfWeek = DayOfWeek.TUESDAY,
        shiftType = ShiftType.NIGHT,
        requiredHeadcount = 2
    )

    @Test
    fun `toDomain mapping correctness`() {
        val domain = testEntity.toDomain()
        assertEquals(testEntity.id, domain.id)
        assertEquals(testEntity.dayOfWeek, domain.dayOfWeek)
        assertEquals(testEntity.shiftType, domain.shiftType)
        assertEquals(testEntity.requiredHeadcount, domain.requiredHeadcount)
    }

    @Test
    fun `toDomain mapping with zero required headcount`() {
        val entity = testEntity.copy(requiredHeadcount = 0)
        val domain = entity.toDomain()
        assertEquals(0, domain.requiredHeadcount)
    }

    @Test
    fun `toDomain mapping with large required headcount`() {
        val entity = testEntity.copy(requiredHeadcount = Int.MAX_VALUE)
        val domain = entity.toDomain()
        assertEquals(Int.MAX_VALUE, domain.requiredHeadcount)
    }

    @Test
    fun `toDomain mapping with default or empty values`() {
        val defaultEntity = ShiftTemplateEntity(id = 0, dayOfWeek = DayOfWeek.SUNDAY, shiftType = ShiftType.MORNING, requiredHeadcount = 0)
        val domain = defaultEntity.toDomain()
        assertEquals(0L, domain.id)
        assertEquals(DayOfWeek.SUNDAY, domain.dayOfWeek)
        assertEquals(ShiftType.MORNING, domain.shiftType)
        assertEquals(0, domain.requiredHeadcount)
    }

    @Test
    fun `toEntity mapping correctness`() {
        val entity = testDomain.toEntity()
        assertEquals(testDomain.id, entity.id)
        assertEquals(testDomain.dayOfWeek, entity.dayOfWeek)
        assertEquals(testDomain.shiftType, entity.shiftType)
        assertEquals(testDomain.requiredHeadcount, entity.requiredHeadcount)
    }

    @Test
    fun `toEntity mapping with zero required headcount`() {
        val domain = testDomain.copy(requiredHeadcount = 0)
        val entity = domain.toEntity()
        assertEquals(0, entity.requiredHeadcount)
    }

    @Test
    fun `toEntity mapping with large required headcount`() {
        val domain = testDomain.copy(requiredHeadcount = Int.MAX_VALUE)
        val entity = domain.toEntity()
        assertEquals(Int.MAX_VALUE, entity.requiredHeadcount)
    }

    @Test
    fun `toEntity mapping with default or empty values`() {
        val defaultDomain = ShiftTemplate(id = 0, dayOfWeek = DayOfWeek.SATURDAY, shiftType = ShiftType.NIGHT, requiredHeadcount = 0)
        val entity = defaultDomain.toEntity()
        assertEquals(0L, entity.id)
        assertEquals(DayOfWeek.SATURDAY, entity.dayOfWeek)
        assertEquals(ShiftType.NIGHT, entity.shiftType)
        assertEquals(0, entity.requiredHeadcount)
    }

    @Test
    fun `toDomain and toEntity round trip conversion`() {
        val roundTripEntity = testEntity.toDomain().toEntity()
        assertEquals(testEntity, roundTripEntity)
    }

    @Test
    fun `toEntity and toDomain round trip conversion`() {
        val roundTripDomain = testDomain.toEntity().toDomain()
        assertEquals(testDomain, roundTripDomain)
    }

}
