package com.example.smartschedule.testutils.base
import com.example.smartschedule.domain.common.Result
import com.google.common.truth.Truth.assertThat
import com.google.common.truth.Truth.assertWithMessage
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.mockito.MockitoAnnotations

abstract class BaseRepositoryTest {

    protected abstract fun setupMocks()

    @Before
    fun baseSetup() {
        MockitoAnnotations.openMocks(this)
        setupMocks()
    }

    /**
     * Asserts that the given [Result] is a success and its data matches the [expectedData].
     *
     * This function first checks if the [result] is a success. If it's an error,
     * the test will fail with a message indicating the unexpected error.
     * If the [result] is a success, it then asserts that the data contained within
     * the [result] is equal to the [expectedData].
     *
     * @param T The type of data expected in the successful result.
     * @param result The [Result] object to be asserted.
     * @param expectedData The data that is expected to be present in the [result] if it's a success.
     */
    protected fun <T> assertSuccess(result: Result<T>, expectedData: T) {
        assertWithMessage("Expected Success but got Error: ${result.exceptionOrNull()}")
            .that(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(expectedData)
    }

    /**
     * Asserts that the given [Result] is a [Result.Success].
     *
     * @param T The type of the data in the [Result].
     * @param result The [Result] to check.
     * @throws AssertionError if the [result] is not a [Result.Success].
     */
    protected fun <T> assertSuccess(result: Result<T>) {
        assertWithMessage("Expected Success but got Error: ${result.exceptionOrNull()}")
            .that(result.isSuccess).isTrue()
    }



    /**
     * Asserts that the given [Result] is a [Result.Error].
     *
     * This function checks if the provided [result] represents an error state.
     * If the [result] is a success, the test will fail with a message
     * indicating the unexpected success and the data it contains.
     *
     * @param result The [Result] object to be asserted.
     * @throws AssertionError if the [result] is not a [Result.Error].
     */
    protected fun assertError(result: Result<*>) {
        assertWithMessage("Expected Error but got Success: ${result.getOrNull()}")
            .that(result.isError).isTrue()
    }

    /**
     * Executes the given [testBody] as a suspend function within a `runTest` coroutine scope.
     * This is a utility function to simplify writing asynchronous tests.
     *
     * @param testBody A suspend lambda function containing the asynchronous test logic.
     */
    protected fun testAsync(testBody: suspend () -> Unit) = runTest {
        testBody()
    }

    /**
     * Asserts that the given [Result] is an error of a specific [Throwable] type [E].
     *
     * This function first checks if the [result] is an error. If it's a success,
     * the test will fail with a message indicating the unexpected success.
     * If the [result] is an error, it then verifies that the exception within the [result]
     * is an instance of the specified [Throwable] type [E].
     *
     * Optionally, if [expectedMessage] is provided, this function will also assert that
     * the message of the exception contains the [expectedMessage].
     *
     * @param E The type of [Throwable] expected in the error result.
     * @param result The [Result] object to be asserted.
     * @param expectedMessage An optional string that the exception's message is expected to contain.
     *                        If null, the exception message is not checked.
     * @throws AssertionError if the [result] is a success, or if the exception is not of type [E],
     *                        or if [expectedMessage] is provided and the exception message does not contain it.
     */
    protected inline fun <reified E : Throwable> assertError(
        result: Result<*>,
        expectedMessage: String? = null
    ) {
        assertWithMessage("Expected Error but got Success: ${result.getOrNull()}")
            .that(result.isError).isTrue()

        val exception = result.exceptionOrNull()
        assertThat(exception).isInstanceOf(E::class.java)

        expectedMessage?.let { message ->
            assertThat(exception?.message).contains(message)
        }
    }

    /**
     * Asserts that a [Throwable] of type [E] is thrown when executing the given [block].
     *
     * This function executes the provided [block] and expects it to throw an exception.
     * It then verifies that the thrown exception is an instance of the specified type [E].
     * If [expectedMessage] is provided, it also asserts that the exception's message
     * contains the [expectedMessage].
     *
     * If no exception is thrown by the [block], this function will throw an [AssertionError]
     * indicating that the expected exception was not thrown.
     *
     * @param E The type of [Throwable] expected to be thrown.
     * @param expectedMessage An optional string that the exception's message is expected to contain.
     *                        If null, the exception message is not checked.
     * @param block The suspendable block of code that is expected to throw an exception.
     * @throws AssertionError if no exception is thrown, or if the thrown exception is not of type [E],
     *                        or if [expectedMessage] is provided and the exception message does not contain it.
     */
    protected suspend inline fun <reified E : Throwable> assertThrows(
        expectedMessage: String? = null,
        crossinline block: suspend () -> Unit
    ) {
        try {
            block()
            throw AssertionError("Expected ${E::class.simpleName} to be thrown, but no exception was thrown")
        } catch (e: Throwable) {
            assertThat(e).isInstanceOf(E::class.java)
            expectedMessage?.let { message ->
                assertThat(e.message).contains(message)
            }
        }
    }

    /**
     * Logs information about the current test being executed.
     * This includes the test name and a brief description of what the test is verifying.
     *
     * @param testName The name of the test method.
     * @param description A short description of the test's purpose.
     */
    protected fun logTestInfo(testName: String, description: String) {
        println("🧪 Running test: $testName")
        println("📝 Description: $description")
        println("=".repeat(50))
    }
}



