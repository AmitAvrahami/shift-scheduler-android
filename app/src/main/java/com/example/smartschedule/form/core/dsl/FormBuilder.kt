package com.example.smartschedule.form.core.dsl

import com.example.smartschedule.form.core.constants.FieldType
import com.example.smartschedule.form.core.interfaces.ValidationResult
import com.example.smartschedule.form.core.interfaces.Validator
import com.example.smartschedule.form.core.types.FieldDefinition
import com.example.smartschedule.form.core.types.FormContext
import com.example.smartschedule.form.core.types.FormSchema
import com.example.smartschedule.form.core.types.FormState
import com.example.smartschedule.form.core.types.ValidationContext
import com.example.smartschedule.form.core.validators.CommonValidators

class FormBuilder {
    private val fields = mutableMapOf<String, FieldDefinition<*>>()

    fun <T> field(
        id: String,
        type: FieldType,
        defaultValue: T? = null,
        required: Boolean = false,
        configure: FieldBuilder<T>.() -> Unit = {},
    ) {
        val builder = FieldBuilder<T>(id)  // העברת ה-ID
        builder.configure()


        fields[id] = FieldDefinition(
            id = id,
            type = type,
            defaultValue = defaultValue,
            required = required,
            validators = builder.validators
        )
    }

    internal fun build(id: String): FormSchema {
        return FormSchema(id, fields.toMap())
    }
}

class FieldBuilder<T>(private val currentFieldId: String) {
    internal val validators = mutableListOf<Validator<T>>()

    fun validator(validator: Validator<T>) {
        validators.add(validator)
    }

    fun required() = validator(createValidator { value, _, _ ->
        when {
            value == null -> ValidationResult.error("שדה זה נדרש")
            value is String && value.isBlank() -> ValidationResult.error("שדה זה נדרש")
            else -> ValidationResult.success()
        }
    })

    fun minLength(min: Int) = validator(createValidator { value, _, _ ->
        when {
            value == null -> ValidationResult.success()
            value !is String -> ValidationResult.error("שדה חייב להיות טקסט")
            value.length < min -> ValidationResult.error("חייב להכיל לפחות $min תווים")
            else -> ValidationResult.success()
        }
    })

    fun maxLength(max: Int) = validator(createValidator { value, _, _ ->
        when {
            value == null -> ValidationResult.success()
            value !is String -> ValidationResult.error("שדה חייב להיות טקסט")
            value.length > max -> ValidationResult.error("מקסימום $max תווים")
            else -> ValidationResult.success()
        }
    })

    fun email() = validator(createValidator { value, _, _ ->
        if (value == null) return@createValidator ValidationResult.success()
        if (value !is String) return@createValidator ValidationResult.error("שדה חייב להיות טקסט")

        val emailRegex = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}".toRegex()
        if (emailRegex.matches(value)) {
            ValidationResult.success()
        } else {
            ValidationResult.error("כתובת אימייל לא תקינה")
        }
    })

    fun israeliPhone() = validator(createValidator { value, _, _ ->
        if (value == null) return@createValidator ValidationResult.success()
        if (value !is String) return@createValidator ValidationResult.error("שדה חייב להיות טקסט")

        val phoneRegex = "^05[0-9]-?[0-9]{7}$".toRegex()
        if (phoneRegex.matches(value.replace("-", ""))) {
            ValidationResult.success()
        } else {
            ValidationResult.error("מספר טלפון לא תקין")
        }
    })

    fun israeliId() = validator(createValidator { value, _, _ ->
        if (value == null) return@createValidator ValidationResult.success()
        if (value !is String) return@createValidator ValidationResult.error("שדה חייב להיות טקסט")

        if (value.length != 9) {
            return@createValidator ValidationResult.error("תעודת זהות חייבת להכיל 9 ספרות")
        }

        val digits = value.map {
            it.digitToIntOrNull() ?: return@createValidator ValidationResult.error("תעודת זהות יכולה להכיל רק ספרות")
        }
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
    })

    fun minValue(min: Double) = validator(createValidator { value, _, _ ->
        when {
            value == null -> ValidationResult.success()
            value !is Number -> ValidationResult.error("שדה חייב להיות מספר")
            value.toDouble() < min -> ValidationResult.error("הערך חייב להיות לפחות $min")
            else -> ValidationResult.success()
        }
    })

    fun maxValue(max: Double) = validator(createValidator { value, _, _ ->
        when {
            value == null -> ValidationResult.success()
            value !is Number -> ValidationResult.error("שדה חייב להיות מספר")
            value.toDouble() > max -> ValidationResult.error("הערך חייב להיות מקסימום $max")
            else -> ValidationResult.success()
        }
    })

    // זה המקום החשוב - custom עם גישה ל-getValue
    fun custom(message: String, predicate: (T?, ValidationContext) -> Boolean) {
        validator(createValidator { value, _, context ->
            if (predicate(value, context)) {
                ValidationResult.success()
            } else {
                ValidationResult.error(message)
            }
        })
    }

    // פונקציה נוחה ליצירת validator
    private fun createValidator(
        validate: (T?, FieldDefinition<T>, ValidationContext) -> ValidationResult
    ): Validator<T> {
        return Validator { value, field, context -> validate(value, field, context) }
    }
}

// 📁 form/core/FormController.kt - עדכון


// DSL function
fun form(id: String, configure: FormBuilder.() -> Unit): FormSchema {
    val builder = FormBuilder()
    builder.configure()
    return builder.build(id)
}

fun FieldBuilder<String>.getValue(fieldId: String,form: FormSchema) {

}
