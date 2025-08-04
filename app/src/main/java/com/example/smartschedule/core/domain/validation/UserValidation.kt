package com.example.smartschedule.core.domain.validation

object UserValidation {

    private val EMAIL_REGEX =
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$".toRegex()

    private val NAME_REGEX =
        "^[א-תa-zA-Z'\\-\\s]{3,}$".toRegex()

    private val EMPLOYEE_NUMBER_REGEX =
        "^\\d{4,}$".toRegex()

    private val STRONG_PASSWORD_REGEX =
        "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#\$%^&+=!]).{6,}$".toRegex()

    const val NAME_VALIDATION = "name_validation"
    const val EMPLOYEE_NUMBER_VALIDATION = "employee_number_validation"
    const val EMAIL_VALIDATION = "email_validation"
    const val PASSWORD_VALIDATION = "password_validation"
    const val MAX_SHIFT_PER_WEEK_VALIDATION = "max_shift_per_week_validation"
    const val CONFIRM_PASSWORD_VALIDATION = "confirm_password_validation"

    fun validateName(username: String): ValidationResult {
        return when {
            username.isBlank() ->
                ValidationResult.Error("השדה שם לא יכול להיות ריק")
            !NAME_REGEX.matches(username) ->
                ValidationResult.Error("שם יכול לכלול רק אותיות, רווחים, מקפים או אפוסטרופים, ובאורך של לפחות 3 תווים")
            else -> ValidationResult.Success
        }
    }



    fun validateEmail(email: String): ValidationResult {
        return when {
            email.isBlank() ->
                ValidationResult.Error("אימייל הוא שדה חובה")
            !EMAIL_REGEX.matches(email) ->
                ValidationResult.Error("פורמט האימייל אינו תקין")
            else -> ValidationResult.Success
        }
    }

    fun validatePassword(password: String): ValidationResult {
        return when {
            password.isBlank() ->
                ValidationResult.Error("סיסמה לא יכולה להיות ריקה")
            !STRONG_PASSWORD_REGEX.matches(password) ->
                ValidationResult.Error("סיסמה חייבת לכלול לפחות 6 תווים, אות קטנה, אות גדולה, מספר ותו מיוחד")
            else -> ValidationResult.Success
        }
    }

    fun validateConfirmPassword(password: String, confirmPassword: String): ValidationResult {
        return when {
            confirmPassword.isBlank() ->
                ValidationResult.Error("אישור סיסמה לא יכול להיות ריק")
            password != confirmPassword ->
                ValidationResult.Error("הסיסמאות אינן תואמות")
            else -> ValidationResult.Success
        }
    }

}