package com.example.smartschedule.form.core.interfaces

import com.example.smartschedule.form.core.types.FieldDefinition
import com.example.smartschedule.form.core.types.ValidationContext

fun interface Validator<T> {
    fun validate(value: T?, field: FieldDefinition<T>, context: ValidationContext): ValidationResult
}

data class ValidationResult(
    val isValid: Boolean,
    val errorMessage: String? = null
) {
    companion object {
        fun success() = ValidationResult(true)
        fun error(message: String) = ValidationResult(false, message)
    }
}