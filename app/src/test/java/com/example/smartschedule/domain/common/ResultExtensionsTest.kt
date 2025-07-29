package com.example.smartschedule.domain.common

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ResultExtensionsTest {

    @Test
    fun `when Success fold called, should execute onSuccess block`(){
        // Arrange
        val result = Result.Success(10)
        var successCalled = false
        var errorCalled = false

        // Act
        result.fold(
            onSuccess = { data ->
                successCalled = true
                assertThat(data).isEqualTo(10)
            },
            onError = {
                errorCalled = true
            }
        )

        // Assert
        assertThat(successCalled).isTrue()
        assertThat(errorCalled).isFalse()
    }


    @Test
    fun `when Error fold called, should execute onError block`() {
        // Arrange
        val exception = RuntimeException("Test error")
        val result = Result.Error(exception)
        var successCalled = false
        var errorCalled = false

        // Act
        result.fold(
            onSuccess = {
                successCalled = true
            },
            onError = { error ->
                errorCalled = true
                assertThat(error).isEqualTo(exception)
            }
        )

        // Assert
        assertThat(successCalled).isFalse()
        assertThat(errorCalled).isTrue()
    }


    // 🎯 map() Extension Tests
    @Test
    fun `when Success map called, should transform data`() {
        // Arrange
        val result = Result.Success(5)

        // Act
        val mapped = result.map { it * 2 }

        // Assert
        assertThat(mapped.isSuccess).isTrue()
        assertThat(mapped.getOrNull()).isEqualTo(10)
    }

    @Test
    fun `when Error map called, should preserve error`() {
        // Arrange
        val exception = RuntimeException("Error")
        val result: Result<Int> = Result.Error(exception)

        // Act
        val mapped = result.map { it * 2 }

        // Assert
        assertThat(mapped.isError).isTrue()
        assertThat(mapped.exceptionOrNull()).isEqualTo(exception)
    }


    // 🎯 onError() Extension Tests
    @Test
    fun `when Success onError called, should not execute action`() {
        // Arrange
        val result = Result.Success("data")
        var actionCalled = false

        // Act
        val returned = result.onError { actionCalled = true }

        // Assert
        assertThat(actionCalled).isFalse()
        assertThat(returned).isSameInstanceAs(result)
    }

    @Test
    fun `when Error onError called, should execute action`() {
        // Arrange
        val exception = RuntimeException("Error")
        val result = Result.Error(exception)
        var capturedError: Throwable? = null

        // Act
        val returned = result.onError { error ->
            capturedError = error
        }

        // Assert
        assertThat(capturedError).isEqualTo(exception)
        assertThat(returned).isSameInstanceAs(result)
    }


    // 🎯 onSuccess() Extension Tests
    @Test
    fun `when Success onSuccess called, should execute action`() {
        // Arrange
        val data = "test data"
        val result = Result.Success(data)
        var capturedData: String? = null

        // Act
        val returned = result.onSuccess { capturedData = it }

        // Assert
        assertThat(capturedData).isEqualTo(data)
        assertThat(returned).isSameInstanceAs(result)
    }

    @Test
    fun `when Error onSuccess called, should not execute action`() {
        // Arrange
        val result: Result<String> = Result.Error(RuntimeException())
        var actionCalled = false

        // Act
        val returned = result.onSuccess { actionCalled = true }

        // Assert
        assertThat(actionCalled).isFalse()
        assertThat(returned).isSameInstanceAs(result)
    }


    // 🎯 getOrDefault() Extension Tests
    @Test
    fun `when Success getOrDefault called, should return data`() {
        // Arrange
        val result = Result.Success("actual")

        // Act
        val actual = result.getOrDefault("default")

        // Assert
        assertThat(actual).isEqualTo("actual")
    }

    @Test
    fun `when Error getOrDefault called, should return default value`() {
        // Arrange
        val result: Result<String> = Result.Error(RuntimeException())

        // Act
        val actual = result.getOrDefault("default")

        // Assert
        assertThat(actual).isEqualTo("default")
    }



}