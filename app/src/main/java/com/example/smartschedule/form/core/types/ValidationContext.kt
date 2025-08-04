package com.example.smartschedule.form.core.types

data class ValidationContext(
    val formContext: FormContext,
    val currentFieldId: String
) {
    fun <R> getValue(fieldId: String): R? {
        return formContext.getCurrentValue<R>(fieldId)
    }
}