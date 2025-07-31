package com.example.smartschedule.testutils.mock

import com.example.smartschedule.domain.common.Result
import com.example.smartschedule.domain.errors.user_error.UserError
import com.example.smartschedule.domain.models.User
import com.example.smartschedule.domain.repository.UserRepository
import kotlinx.coroutines.flow.flowOf
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever


class MockUserRepositoryBuilder {
    private val mockRepository = mock<UserRepository>()

  /**
   * Configures the mock to return a successful registration result.
   *
   * The `registerUserWithResult` function will return `Result.Success` with the provided user.
   *
   * @param user The user to be returned as a result of the successful registration.
   * @return An instance of the Builder for further chaining.
   */
  suspend fun withSuccessfulRegistration(user: User): MockUserRepositoryBuilder {
        whenever(mockRepository.registerUserWithResult(any(), any()))
            .thenReturn(Result.Success(user))
        return this
    }

    /**
     * Configures the mock to return a [Result.Error] with [UserError.DuplicateEmail]
     * when [UserRepository.registerUserWithResult] is called.
     *
     * @param email The email that already exists in the system.
     * @return [MockUserRepositoryBuilder] to allow for further mock configuration.
     */
    suspend fun withDuplicateEmailError(email: String): MockUserRepositoryBuilder {
        whenever(mockRepository.registerUserWithResult(any(), any()))
            .thenReturn(Result.Error(UserError.DuplicateEmail(email)))
        return this
    }

    /**
     * Configures the mock's behavior for checking email existence.
     *
     * @param email The email to check.
     * @param exists Whether the email exists (default: true).
     * @return The builder itself, to allow for method chaining.
     */
    suspend fun withEmailExists(email: String, exists: Boolean = true): MockUserRepositoryBuilder {
        whenever(mockRepository.isEmailExistsWithResult(email))
            .thenReturn(Result.Success(exists))
        return this
    }

    /**
     * Configures the `loginWithResult` function to return a successful login with user details.
     *
     * This function is used to simulate a scenario where logging into the system is successful.
     * When the `loginWithResult` function of the `mockRepository` is called with the provided email and password,
     * it will return a [Result.Success] with the provided [User] object.
     *
     * @param email The user's email for login.
     * @param password The user's password for login.
     * @param user The [User] object to be returned in case of a successful login.
     * @return The current [MockUserRepositoryBuilder], allowing for function chaining.
     */
    suspend fun withSuccessfulLogin(email: String, password: String, user: User): MockUserRepositoryBuilder {
        whenever(mockRepository.loginWithResult(email, password))
            .thenReturn(Result.Success(user))
        return this
    }

    /**
     * Configures the mock's behavior for an unsuccessful login attempt due to invalid credentials.
     * When the `loginWithResult` function is called with the specified email and password,
     * it will return a `UserError.InvalidCredentials` error.
     *
     * @param email The email to be checked.
     * @param password The password to be checked.
     * @return The current instance of `MockUserRepositoryBuilder` for chaining.
     */
    suspend fun withInvalidCredentials(email: String, password: String): MockUserRepositoryBuilder {
        whenever(mockRepository.loginWithResult(email, password))
            .thenReturn(Result.Error(UserError.InvalidCredentials(email)))
        return this
    }

    /**
     * Configures the mock to return a DatabaseCorrupted error for all functions.
     *
     * Suitable for testing scenarios where the database is corrupted.
     * @return [MockUserRepositoryBuilder] - The current builder object for further construction.
     */
    suspend fun withDatabaseCorrupted(): MockUserRepositoryBuilder {
        whenever(mockRepository.registerUserWithResult(any(), any()))
            .thenReturn(Result.Error(UserError.DatabaseCorrupted))
        whenever(mockRepository.loginWithResult(any(), any()))
            .thenReturn(Result.Error(UserError.DatabaseCorrupted))
        whenever(mockRepository.isEmailExistsWithResult(any()))
            .thenReturn(Result.Error(UserError.DatabaseCorrupted))
        return this
    }

     /**
      * Mocks the `getAllUsers` method to return a specific list of users.
      * This is useful for testing scenarios that involve retrieving all users from the repository.
      *
      * @param users The list of [User] objects to be returned when `getAllUsers` is called.
      * @return The current [MockUserRepositoryBuilder] instance for chaining.
      */
     fun withUsersList(users: List<User>): MockUserRepositoryBuilder {
        whenever(mockRepository.getAllUsers())
            .thenReturn(flowOf(users))
        return this
    }

    /**
     * Builder Pattern for a mock of UserRepository - allows for easy and clear creation of mocks.
     *
     * This Builder Pattern is a design pattern that helps us build complex objects step by step.
     * Instead of remembering all the parameters, we call functions with clear names.
     *
     * Example usage:
     * ```
     * val mockRepo = MockUserRepositoryBuilder()
     *     .withSuccessfulRegistration(user)
     *     .withEmailExists("amit@test.com")
     *     .build()
     * ```
     */
    fun build(): UserRepository = mockRepository

}