package com.example.smartschedule.domain.usecase

import com.example.smartschedule.domain.repository.UserRepository
import com.example.smartschedule.testutils.TestDataFactory
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever
import com.example.smartschedule.domain.common.Result
import com.example.smartschedule.domain.errors.user_error.UserError
import com.google.common.truth.Truth.assertThat
import org.mockito.kotlin.verify

class LoginUseCaseTest {

    @Mock
    private lateinit var userRepository: UserRepository
    private lateinit var loginUseCase: LoginUseCase

    @Before
    fun setup(){
        MockitoAnnotations.openMocks(this)
        loginUseCase = LoginUseCase(userRepository)
    }

    @Test
    fun `when login with valid credentials, should return success with user`() = runTest {
        // Arrange
        val email = "amit@test.com"
        val password = "123456"
        val expectedUser = TestDataFactory.createUser(email = email)

        whenever(userRepository.loginWithResult(email, password))
            .thenReturn(Result.Success(expectedUser))

        // Act
        val result = loginUseCase.executeWithResult(email, password)

        // Assert
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(expectedUser)
    }

    @Test
    fun `when login with invalid credentials, should return error`() = runTest {
        // Arrange
        val email = "wrong@test.com"
        val password = "wrong123"
        val expectedError = UserError.InvalidCredentials(email)

        whenever(userRepository.loginWithResult(email, password))
            .thenReturn(Result.Error(expectedError))

        // Act
        val result = loginUseCase.executeWithResult(email, password)

        // Assert
        assertThat(result.isError).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(UserError.InvalidCredentials::class.java)
    }

    @Test
    fun `when email is blank, should return validation error`() = runTest {
        // Arrange
        val email = ""
        val password = "123456"

        // Act
        val result = loginUseCase.executeWithResult(email, password)

        // Assert
        assertThat(result.isError).isTrue()
        assertThat(result.exceptionOrNull()?.message).contains("כתובת אימייל נדרשת")
    }

    @Test
    fun `when password is blank, should return validation error`() = runTest {
        // Arrange
        val email = "amit@test.com"
        val password = ""

        // Act
        val result = loginUseCase.executeWithResult(email, password)

        // Assert
        assertThat(result.isError).isTrue()
        assertThat(result.exceptionOrNull()?.message).contains("סיסמה נדרשת")
    }

    @Test
    fun `when repository throws database error, should return error`() = runTest {
        // Arrange
        val email = "amit@test.com"
        val password = "123456"
        val databaseError = UserError.DatabaseCorrupted

        whenever(userRepository.loginWithResult(email, password))
            .thenReturn(Result.Error(databaseError))

        // Act
        val result = loginUseCase.executeWithResult(email, password)

        // Assert
        assertThat(result.isError).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(databaseError)
    }

    @Test
    fun `when called with valid input, should call repository with same parameters`() = runTest {
        // Arrange
        val email = "amit@test.com"
        val password = "123456"
        val user = TestDataFactory.createUser(email = email)

        whenever(userRepository.loginWithResult(email, password))
            .thenReturn(Result.Success(user))

        // Act
        loginUseCase.executeWithResult(email, password)

        // Assert - וידוא שנקרא עם הפרמטרים הנכונים
        verify(userRepository).loginWithResult(email, password)
    }
}