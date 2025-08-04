package com.example.smartschedule.form.core.validators

import com.example.smartschedule.form.core.interfaces.ValidationResult
import com.example.smartschedule.form.core.interfaces.Validator

object CommonValidators {

    fun required() = Validator<Any?> { value ,_,_ ->
        when {
            value == null -> ValidationResult.error("שדה זה נדרש")
            value is String && value.isBlank() -> ValidationResult.error("שדה זה נדרש")
            else -> ValidationResult.success()
        }
    }

    fun minLength(min: Int) = Validator<String?> { value ,_,_->
        when {
            value == null -> ValidationResult.success()
            value.length < min -> ValidationResult.error("חייב להכיל לפחות $min תווים")
            else -> ValidationResult.success()
        }
    }

    fun maxLength(max: Int) = Validator<String?> { value,_,_ ->
        when {
            value == null -> ValidationResult.success()
            value.length > max -> ValidationResult.error("מקסימום $max תווים")
            else -> ValidationResult.success()
        }
    }

    fun email() = Validator<String?> { value ,_,_->
        if (value == null) return@Validator ValidationResult.success()

        val emailRegex = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}".toRegex()
        if (emailRegex.matches(value)) {
            ValidationResult.success()
        } else {
            ValidationResult.error("כתובת אימייל לא תקינה")
        }
    }

    fun israeliPhone() = Validator<String?> { value ,_,_->
        if (value == null) return@Validator ValidationResult.success()

        val phoneRegex = "^05[0-9]-?[0-9]{7}$".toRegex()
        if (phoneRegex.matches(value.replace("-", ""))) {
            ValidationResult.success()
        } else {
            ValidationResult.error("מספר טלפון לא תקין")
        }
    }

    fun israeliId() = Validator<String?> { value,_,_ ->
        if (value == null) return@Validator ValidationResult.success()

        if (value.length != 9) {
            return@Validator ValidationResult.error("תעודת זהות חייבת להכיל 9 ספרות")
        }

        // Israeli ID validation algorithm
        val digits = value.map { it.digitToIntOrNull() ?: return@Validator ValidationResult.error("תעודת זהות יכולה להכיל רק ספרות") }
        var sum = 0

        for (i in 0..7) {
            var digit = digits[i]
            if (i % 2 == 1) digit *= 2
            if (digit > 9) digit = digit / 10 + digit % 10
            sum += digit
        }

        val checkDigit = (10 - (sum % 10)) % 10
        if (checkDigit == digits[8]) {
            ValidationResult.success()
        } else {
            ValidationResult.error("תעודת זהות לא תקינה")
        }
    }

    fun minValue(min: Double) = Validator<Number?> { value,_,_ ->
        when {
            value == null -> ValidationResult.success()
            value.toDouble() < min -> ValidationResult.error("הערך חייב להיות לפחות $min")
            else -> ValidationResult.success()
        }
    }

    fun maxValue(max: Double) = Validator<Number?> { value,_,_ ->
        when {
            value == null -> ValidationResult.success()
            value.toDouble() > max -> ValidationResult.error("הערך חייב להיות מקסימום $max")
            else -> ValidationResult.success()
        }
    }

    fun custom(message: String, predicate: (Any?) -> Boolean) = Validator<Any?> { value,_,_ ->
        if (predicate(value)) {
            ValidationResult.success()
        } else {
            ValidationResult.error(message)
        }
    }
}