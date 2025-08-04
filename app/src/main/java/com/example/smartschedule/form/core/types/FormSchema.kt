package com.example.smartschedule.form.core.types

/**
 * Represents the schema of a form.
 *
 * @property id The unique identifier of the form schema.
 * @property fields A map of field definitions, where the key is the field ID and the value is the [FieldDefinition].
 */
data class FormSchema(
    val id: String,
    val fields: Map<String, FieldDefinition<*>>
) {
    inline fun <reified T> getField(fieldId: String): FieldDefinition<T>? {
        return fields[fieldId] as? FieldDefinition<T>
    }
}