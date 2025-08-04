package com.example.smartschedule.form.core.types

import com.example.smartschedule.form.core.constants.FieldType
import com.example.smartschedule.form.core.interfaces.Validator

data class FieldDefinition<T>(
    val id: String,
    val type: FieldType,
    val defaultValue: T? = null,
    val required: Boolean = false,
    val validators: List<Validator<T>> = emptyList()
)