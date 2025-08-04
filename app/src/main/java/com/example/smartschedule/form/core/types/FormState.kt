package com.example.smartschedule.form.core.types

class FormState {
    private val _values = mutableMapOf<String, Any?>()
    private val _errors = mutableMapOf<String, String?>()
    private val _touched = mutableMapOf<String, Boolean>()

    val values: Map<String, Any?> get() = _values.toMap()
    val errors: Map<String, String?> get() = _errors.toMap()
    val touched: Map<String, Boolean> get() = _touched.toMap()

    fun <T> getValue(fieldId: String): T? = _values[fieldId] as? T

    fun <T> setValue(fieldId: String, value: T?) {
        _values[fieldId] = value
        _touched[fieldId] = true
    }

    fun setError(fieldId: String, error: String?) {
        _errors[fieldId] = error
    }

    fun clearError(fieldId: String) {
        _errors.remove(fieldId)
    }

    fun hasErrors(): Boolean = _errors.values.any { it != null }

    fun getError(fieldId: String): String? = _errors[fieldId]

    fun isTouched(fieldId: String): Boolean = _touched[fieldId] ?: false

    fun reset() {
        _values.clear()
        _errors.clear()
        _touched.clear()
    }
}