package com.example.smartschedule.domain.common

import android.database.sqlite.SQLiteDatabaseCorruptException
import android.database.sqlite.SQLiteException
import com.example.smartschedule.domain.errors.employee_error.EmployeeError

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

// Validation wrapper
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