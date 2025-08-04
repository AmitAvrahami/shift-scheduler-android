package com.example.smartschedule.core.domain.errors.employee_error

sealed class EmployeeError(message: String, cause: Throwable? = null) : Exception(message, cause) {

    // Database errors
    object DatabaseCorrupted : EmployeeError("Database is corrupted") {
        private fun readResolve(): Any = DatabaseCorrupted
    }

    data class DatabaseError(val originalError: Throwable) : EmployeeError("Database operation failed", originalError)

    // Business logic errors
    data class EmployeeNotFound(val employeeId: String) : EmployeeError("Employee with ID $employeeId not found")
    data class DuplicateEmployeeNumber(val employeeNumber: String) : EmployeeError("Employee number $employeeNumber already exists")

    // Validation errors
    data class ValidationError(val field: String, val reason: String) : EmployeeError("$field: $reason")

    // Network errors (for future use)
    object NetworkUnavailable : EmployeeError("Network connection is not available") {
        private fun readResolve(): Any = NetworkUnavailable
    }

    data class ServerError(val code: Int) : EmployeeError("Server error: $code")
}
