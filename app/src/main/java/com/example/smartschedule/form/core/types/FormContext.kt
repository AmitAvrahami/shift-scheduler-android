package com.example.smartschedule.form.core.types

    class FormContext {
        private val currentValues = mutableMapOf<String, Any?>()

        fun <T> getCurrentValue(fieldId: String): T? {
            return currentValues[fieldId] as? T
        }

        fun setValue(fieldId: String, value: Any?) {
            currentValues[fieldId] = value
        }

        fun getAllValues(): Map<String, Any?> = currentValues.toMap()
    }

