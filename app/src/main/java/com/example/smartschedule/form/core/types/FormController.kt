package com.example.smartschedule.form.core.types

class FormController(private val _schema: FormSchema) {
    val state = FormState()
    private val formContext = FormContext()
    val schema: FormSchema get() = _schema



    fun <T> updateField(fieldId: String, value: T?) {
        state.setValue(fieldId, value)
        formContext.setValue(fieldId, value)  // עדכון גם בהקשר
        validateField(fieldId, value)
    }

    private inline fun <reified T> validateField(fieldId: String, value: T?) {
        val field = _schema.getField<T>(fieldId) ?: return

        val validationContext = ValidationContext(formContext, fieldId)

        // בדיקת required
        if (field.required && value == null) {
            state.setError(fieldId, "שדה זה נדרש")
            return
        }

        // הרצת הולידטורים
        val firstError = field.validators
            .map { it.validate(value, field, validationContext) }
            .firstOrNull { !it.isValid }

        if (firstError != null) {
            state.setError(fieldId, firstError.errorMessage)
        } else {
            state.clearError(fieldId)
        }
    }

    fun validateAll(): Boolean {
        _schema.fields.forEach { (fieldId, _) ->
            val value = state.getValue<Any?>(fieldId)
            validateField(fieldId, value)

        }
        return !state.hasErrors()
    }

    fun submit(): Map<String, Any?> {
        return if (validateAll()) {
            state.values
        } else {
            throw IllegalStateException("Form has validation errors")
        }
    }
}
