package com.example.smartschedule.domain.validation

import com.example.smartschedule.core.domain.validation.ShiftValidation
import com.example.smartschedule.testutils.TestDataFactory
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.time.LocalDateTime

class ShiftValidationTest {

    @Test
    fun `when shift start before it ends,should return truth`(){
        //Arrange
        val shift = TestDataFactory.createShift()

        //Act
        val result = ShiftValidation.isValidShift(shift)

        //Assert
        assertThat(result).isTrue()
    }

    @Test
    fun `when shift end before it starts,should return false`(){
        //Arrange
        val shift = TestDataFactory.createShift(
            startTime = LocalDateTime.of(2025, 1, 28, 16, 0),
            endTime = LocalDateTime.of(2025, 1, 28, 8, 0) // לפני הstart!
        )

        //Act
        val result = ShiftValidation.isValidShift(shift)

        //Assert
        assertThat(result).isFalse()
    }

    @Test
    fun `when shift duration is exactly 8 hours, should be standard shift`() {
        // Arrange
        val shift = TestDataFactory.createMorningShift()

        // Act
        val result = ShiftValidation.isStandardShift(shift)

        // Assert
        assertThat(result).isTrue()
    }

    @Test
    fun `when shift duration is 6 hours, should not be standard shift`() {
        // Arrange
        val startTime = LocalDateTime.of(2025, 1, 28, 8, 0)
        val endTime = LocalDateTime.of(2025, 1, 28, 14, 0) // רק 6 שעות
        val shift = TestDataFactory.createShift(
            startTime = startTime,
            endTime = endTime
        )

        // Act
        val result = ShiftValidation.isStandardShift(shift)

        // Assert
        assertThat(result).isFalse()
    }

    @Test
    fun `when end time is before start time, should return validation error`() {
        // Arrange
        val startTime = LocalDateTime.of(2025, 1, 28, 16, 0)
        val endTime = LocalDateTime.of(2025, 1, 28, 8, 0)

        // Act
        val result = ShiftValidation.validateShiftTimes(startTime, endTime)

        // Assert
        assertThat(result).isNotNull()
        assertThat(result).contains("שעת סיום חייבת להיות אחרי שעת התחלה")

    }


    @Test
    fun `when start and end times are equal, should return validation error`() {
        // Arrange
        val time = LocalDateTime.of(2025, 1, 28, 12, 0)

        // Act
        val result = ShiftValidation.validateShiftTimes(time, time)

        // Assert
        assertThat(result).isNotNull()
        assertThat(result).contains("שעת התחלה ושעת הסיום לא יכולות להיות זהות")
    }

    @Test
    fun `when times are valid, should return no error`() {
        // Arrange
        val startTime = LocalDateTime.of(2025, 1, 28, 8, 0)
        val endTime = LocalDateTime.of(2025, 1, 28, 16, 0)

        // Act
        val result = ShiftValidation.validateShiftTimes(startTime, endTime)

        // Assert
        assertThat(result).isNull()
    }

}