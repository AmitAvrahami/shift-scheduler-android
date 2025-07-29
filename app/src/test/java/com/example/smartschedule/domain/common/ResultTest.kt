package com.example.smartschedule.domain.common

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ResultTest {

    // 🎯 Basic Result Creation Tests
    @Test
    fun `when creating Success result, should contain correct data`() {
        // Arrange
        val data = "test data"

        // Act
        val result = Result.Success(data)

        // Assert
        assertThat(result.data).isEqualTo(data)
        assertThat(result.isSuccess).isTrue()
        assertThat(result.isError).isFalse()
    }

    @Test
    fun `when creating Error result, should contain correct exception`() {
        // Arrange
        val exception = RuntimeException("Test error")

        // Act
        val result = Result.Error(exception)

        // Assert
        assertThat(result.exception).isEqualTo(exception)
        assertThat(result.isSuccess).isFalse()
        assertThat(result.isError).isTrue()
    }

    // 🎯 getOrNull Tests
    @Test
    fun `when Success getOrNull called, should return data`() {
        // Arrange
        val data = 42
        val result = Result.Success(data)

        // Act
        val actual = result.getOrNull()

        // Assert
        assertThat(actual).isEqualTo(data)
    }

    @Test
    fun `when Error getOrNull called, should return null`() {
        // Arrange
        val result: Result<String> = Result.Error(RuntimeException("Error"))

        // Act
        val actual = result.getOrNull()

        // Assert
        assertThat(actual).isNull()


    }

    // 🎯 exceptionOrNull Tests
    @Test
    fun `when Success exceptionOrNull called, should return null`() {
        // Arrange
        val result = Result.Success("data")

        // Act
        val actual = result.exceptionOrNull()

        // Assert
        assertThat(actual).isNull()
    }

    @Test
    fun `when Error exceptionOrNull called, should return exception`() {
        // Arrange
        val exception = IllegalArgumentException("Test")
        val result = Result.Error(exception)

        // Act
        val actual = result.exceptionOrNull()

        // Assert
        assertThat(actual).isEqualTo(exception)
    }
}