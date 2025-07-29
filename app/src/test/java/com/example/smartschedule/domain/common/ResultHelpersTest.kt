package com.example.smartschedule.domain.common

import com.example.smartschedule.domain.errors.employee_error.EmployeeError
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Test

class ResultHelpersTest {

    // 🎯 safeDbOperation Tests
    @Test
    fun `when safeDbOperation succeeds, should return Success`() = runTest {
        // Arrange
        val expectedData = "success data"

        // Act
        val result = safeDbOperation {
            expectedData
        }

        // Assert
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(expectedData)
    }

    @Test
    fun `when safeDbOperation throws exception, should return Error`() = runTest {
        // Arrange
        val exception = RuntimeException("Database error")

        // Act
        val result = safeDbOperation<String> {
            throw exception
        }

        // Assert
        assertThat(result.isError).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(EmployeeError.DatabaseError::class.java)
    }

    // 🎯 validateAndWrap Tests
    @Test
    fun `when validation passes, should return Success`() {
        // Arrange
        val data = "valid data"
        val validation: (String) -> String? = { null } // no error

        // Act
        val result = validateAndWrap(data, validation)

        // Assert
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(data)
    }

    @Test
    fun `when validation fails, should return Error`() {
        // Arrange
        val data = "invalid data"
        val errorMessage = "Validation failed"
        val validation: (String) -> String? = { errorMessage }

        // Act
        val result = validateAndWrap(data, validation)

        // Assert
        assertThat(result.isError).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(EmployeeError.ValidationError::class.java)
    }

    // 🎯 Kotlin Result to Result conversion
    @Test
    fun `when Kotlin Result is success, should convert to Success`() {
        // Arrange
        val data = 42
        val kotlinResult = kotlin.Result.success(data)

        // Act
        val result = kotlinResult.toResult()

        // Assert
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(data)
    }

    @Test
    fun `when Kotlin Result is failure, should convert to Error`() {
        // Arrange
        val exception = IllegalStateException("Error")
        val kotlinResult = kotlin.Result.failure<String>(exception)

        // Act
        val result = kotlinResult.toResult()

        // Assert
        assertThat(result.isError).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(exception)
    }
}