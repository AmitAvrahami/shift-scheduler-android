package com.smartschedule.domain.use_cases.common

import com.smartschedule.utils.Resource

/**
 * A utility function that wraps a suspend block of code in a try-catch block,
 * returning a [Resource] object that represents either success or failure.
 * It provides a standardized way to handle exceptions in use cases and repositories.
 *
 * @param T The type of the data to be returned in the [Resource.Success] case.
 * @param validate An optional lambda function that performs validation before executing the main block.
 *                 If validation fails, it should throw an exception (e.g., [IllegalArgumentException]).
 * @param block The suspend lambda function to be executed. This is where the main logic
 *              (e.g., a network call or database query) resides.
 * @return A [Resource.Success] containing the result of the `block` if it executes successfully,
 *         or a [Resource.Error] with a descriptive message if an exception is caught.
 *         It specifically handles [IllegalArgumentException] with a custom message.
 */
suspend fun <T> safeCall(
    validate :  (() -> Unit)? = null,
    block : suspend () -> T
): Resource<T>{
    return try {
        validate?.invoke()

        val result = block()
        Resource.Success(result)
    }catch (e: IllegalArgumentException) {
        Resource.Error("⚠️ נתונים לא חוקיים: ${e.message}")
    } catch (e: Exception) {
        Resource.Error(e.message ?: "❌ שגיאה לא צפויה בעת ביצוע הפעולה")
    }
}





/**
 * A wrapper function that executes a suspend block of code and handles potential exceptions,
 * specifically designed for operations that return a list of items. It returns a [Resource]
 * object representing the outcome: [Resource.Success] with the resulting list or [Resource.Error]
 * if an exception occurs.
 *
 * This function is similar to [safeCall] but is intended for list-returning operations,
 * providing a more specific name for clarity in use cases like fetching data from a repository.
 *
 * It can optionally run a validation block before executing the main operation.
 * If the validation fails by throwing an [IllegalArgumentException], it's caught and
 * wrapped in a [Resource.Error]. Other exceptions are also caught and wrapped.
 *
 * @param T The type of items within the list returned by the operation.
 * @param validate An optional lambda function that performs validation checks before the main block is executed.
 *                 It should throw an [IllegalArgumentException] if validation fails. Defaults to `null`.
 * @param block The suspend lambda function to be executed, which is expected to return a `List<T>`.
 * @return [Resource.Success<List<T>>] containing the list if the operation is successful.
 *         [Resource.Error] containing an error message if any exception is caught.
 */
suspend fun <T> safeListCall(
    validate: (() -> Unit)? = null,
    block: suspend () -> List<T>
): Resource<List<T>> {
    return try {
        validate?.invoke()

        val result = block()

        if (result.isEmpty()) {
            Resource.Error("הרשימה ריקה – לא נמצאו נתונים")
        } else {
            Resource.Success(result)
        }
    } catch (e: IllegalArgumentException) {
        Resource.Error("⚠️ נתונים לא חוקיים: ${e.message}")
    } catch (e: Exception) {
        Resource.Error(e.message ?: "❌ שגיאה לא צפויה בעת שליפת נתונים")
    }
}
