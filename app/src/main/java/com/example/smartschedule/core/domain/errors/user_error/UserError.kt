package com.example.smartschedule.core.domain.errors.user_error

sealed class UserError(message: String, cause: Throwable? = null) : Exception(message, cause) {

    //DataBase Error
    object DatabaseCorrupted : UserError("מסד הנתונים של המשתמש פגום") {
        private fun readResolve(): Any = DatabaseCorrupted
    }

    data class DatabaseError(val originalError: Throwable) :
        UserError("פעולת מסד הנתונים של המשתמש נכשלה", originalError)

    // Authentication errors
    data class UserNotFound(val identifier: String) : UserError("User with identifier $identifier not found")
    data class InvalidCredentials(val email: String) : UserError("Invalid credentials for user $email")
    object UserNotLoggedIn : UserError("No user is currently logged in") {
        private fun readResolve(): Any = UserNotLoggedIn
    }

    // Registration errors
    data class DuplicateEmail(val email: String) : UserError("Email $email already exists in the system")
    data class PasswordHashFailure(val originalError: Throwable) : UserError("Failed to hash password", originalError)

    // Validation errors
    data class ValidationError(val field: String, val reason: String) : UserError("$field: $reason")

    // Network errors (for future use)
    object NetworkUnavailable : UserError("Network connection is not available") {
        private fun readResolve(): Any = NetworkUnavailable
    }

    data class ServerError(val code: Int) : UserError("Server error: $code")

}