package com.smartschedule.data.mappers

import com.smartschedule.data.local.entities.ShiftEntity
import com.smartschedule.domain.models.Shift
import com.smartschedule.domain.models.ShiftType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.time.LocalDate

class ShiftMapperKtTest {

    private val testEntity = ShiftEntity(
        id = 1L,
        workScheduleId = 10L,
        date = LocalDate.of(2024, 5, 20),
        shiftType = ShiftType.MORNING,
        requiredHeadcount = 5,
        notes = "Standard morning shift"
    )

    private val testDomain = Shift(
        id = 2L,
        workScheduleId = 11L,
        date = LocalDate.of(2024, 5, 21),
        shiftType = ShiftType.NIGHT,
        requiredHeadcount = 2,
        notes = "Night shift, be prepared"
    )

    @Test
    fun `toDomain mapping correctness`() {
        val domain = testEntity.toDomain()
        assertEquals(testEntity.id, domain.id)
        assertEquals(testEntity.workScheduleId, domain.workScheduleId)
        assertEquals(testEntity.date, domain.date)
        assertEquals(testEntity.shiftType, domain.shiftType)
        assertEquals(testEntity.requiredHeadcount, domain.requiredHeadcount)
        assertEquals(testEntity.notes, domain.notes)
    }

    @Test
    fun `toDomain with nullable notes`() {
        val entity = testEntity.copy(notes = null)
        val domain = entity.toDomain()
        assertNull(domain.notes)
    }

    @Test
    fun `toDomain with empty notes`() {
        val entity = testEntity.copy(notes = "")
        val domain = entity.toDomain()
        assertEquals("", domain.notes)
    }

    @Test
    fun `toDomain with long notes`() {
        val longNote = "a".repeat(1000)
        val entity = testEntity.copy(notes = longNote)
        val domain = entity.toDomain()
        assertEquals(longNote, domain.notes)
    }

    @Test
    fun `toDomain with special characters in notes`() {
        val specialNote = "Notes with !@#$%^&*() and unicode: ö, ä, ü, ß and emojis 😃🎉"
        val entity = testEntity.copy(notes = specialNote)
        val domain = entity.toDomain()
        assertEquals(specialNote, domain.notes)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `toDomain with zero required headcount`() {
        val entity = testEntity.copy(requiredHeadcount = 0)
        entity.toDomain()
    }

    @Test
    fun `toDomain with maximum integer required headcount`() {
        val entity = testEntity.copy(requiredHeadcount = Int.MAX_VALUE)
        val domain = entity.toDomain()
        assertEquals(Int.MAX_VALUE, domain.requiredHeadcount)
    }

    @Test
    fun `toDomain with different valid dates`() {
        val leapDate = LocalDate.of(2024, 2, 29)
        val entityLeap = testEntity.copy(date = leapDate)
        val domainLeap = entityLeap.toDomain()
        assertEquals(leapDate, domainLeap.date)

        val endOfMonthDate = LocalDate.of(2024, 1, 31)
        val entityEndOfMonth = testEntity.copy(date = endOfMonthDate)
        val domainEndOfMonth = entityEndOfMonth.toDomain()
        assertEquals(endOfMonthDate, domainEndOfMonth.date)
    }

    @Test
    fun `toEntity mapping correctness`() {
        val entity = testDomain.toEntity()
        assertEquals(testDomain.id, entity.id)
        assertEquals(testDomain.workScheduleId, entity.workScheduleId)
        assertEquals(testDomain.date, entity.date)
        assertEquals(testDomain.shiftType, entity.shiftType)
        assertEquals(testDomain.requiredHeadcount, entity.requiredHeadcount)
        assertEquals(testDomain.notes, entity.notes)
    }

    @Test
    fun `toEntity with nullable notes`() {
        val domain = testDomain.copy(notes = null)
        val entity = domain.toEntity()
        assertNull(entity.notes)
    }

    @Test
    fun `toEntity with empty notes`() {
        val domain = testDomain.copy(notes = "")
        val entity = domain.toEntity()
        assertEquals("", entity.notes)
    }

    @Test
    fun `toEntity with long notes`() {
        val longNote = "b".repeat(1000)
        val domain = testDomain.copy(notes = longNote)
        val entity = domain.toEntity()
        assertEquals(longNote, entity.notes)
    }

    @Test
    fun `toEntity with special characters in notes`() {
        val specialNote = "Entity notes with !@#$%^&*() and unicode: ö, ä, ü, ß and emojis 😃🎉"
        val domain = testDomain.copy(notes = specialNote)
        val entity = domain.toEntity()
        assertEquals(specialNote, entity.notes)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `toEntity with zero required headcount`() {
        val domainWithZeroHeadcount = testDomain.copy(requiredHeadcount = 0)
        domainWithZeroHeadcount.toEntity()
    }

    @Test
    fun `toEntity with maximum integer required headcount`() {
        val domain = testDomain.copy(requiredHeadcount = Int.MAX_VALUE)
        val entity = domain.toEntity()
        assertEquals(Int.MAX_VALUE, entity.requiredHeadcount)
    }

    @Test
    fun `toEntity with different valid dates`() {
        val leapDate = LocalDate.of(2020, 2, 29)
        val domainLeap = testDomain.copy(date = leapDate)
        val entityLeap = domainLeap.toEntity()
        assertEquals(leapDate, entityLeap.date)

        val startOfMonthDate = LocalDate.of(2023, 12, 1)
        val domainStartOfMonth = testDomain.copy(date = startOfMonthDate)
        val entityStartOfMonth = domainStartOfMonth.toEntity()
        assertEquals(startOfMonthDate, entityStartOfMonth.date)
    }

    @Test
    fun `Round trip mapping integrity`() {
        val roundTripEntity = testEntity.toDomain().toEntity()
        assertEquals(testEntity, roundTripEntity)
    }

    @Test
    fun `Round trip mapping integrity from domain`() {
        val roundTripDomain = testDomain.toEntity().toDomain()
        assertEquals(testDomain, roundTripDomain)
    }
}
