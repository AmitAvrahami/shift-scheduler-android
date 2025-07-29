package com.example.smartschedule.domain.common

import android.database.sqlite.SQLiteDatabaseCorruptException
import android.database.sqlite.SQLiteException
import com.example.smartschedule.domain.errors.employee_error.EmployeeError

/**
 * Executes a database operation and wraps the result in a [Result] object, handling potential exceptions.
 *
 * This function is designed to safely perform database operations by catching common database-related exceptions
 * and mapping them to specific error types defined in [EmployeeError].
 *
 * @param T The type of the result expected from the database operation.
 * @param operation A suspend function representing the database operation to be executed.
 * @return A [Result.Success] containing the result of the operation if it completes successfully.
 *         A [Result.Error] containing a specific [EmployeeError] if an exception occurs:
 *         - [EmployeeError.DatabaseCorrupted] if an [SQLiteException] or [SQLiteDatabaseCorruptException] is caught.
 *         - [EmployeeError.DatabaseError] wrapping the original exception for any other [Exception].
 */
suspend inline fun <T> safeDbOperation(
    operation: suspend () -> T
): Result<T> {
    return try {
        Result.Success(operation())
    } catch (e: SQLiteException) {
        Result.Error(EmployeeError.DatabaseCorrupted)
    } catch (e: SQLiteDatabaseCorruptException) {
        Result.Error(EmployeeError.DatabaseCorrupted)
    } catch (e: Exception) {
        Result.Error(EmployeeError.DatabaseError(e))
    }
}

/**
 * Wraps data in a Result after validating it.
 *
 * @param data The data to validate and wrap.
 * @param validation A function that takes the data and returns an error message string if validation fails, or null if it succeeds.
 * @return A Result.Success containing the data if validation passes, or a Result.Error with a ValidationError if it fails.
 */// Validation wrapper
fun <T> validateAndWrap(
    data: T,
    validation: (T) -> String?
): Result<T> {
    val error = validation(data)
    return if (error != null) {
        Result.Error(EmployeeError.ValidationError("validation", error))
    } else {
        Result.Success(data)
    }
}

// Convert Kotlin Result to our Result
fun <T> kotlin.Result<T>.toResult(): Result<T> {
    return fold(
        onSuccess = { Result.Success(it) },
        onFailure = { Result.Error(it) }
    )
}