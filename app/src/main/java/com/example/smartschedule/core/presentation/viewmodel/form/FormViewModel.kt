package com.example.smartschedule.core.presentation.viewmodel.form

import androidx.lifecycle.ViewModel
import com.example.smartschedule.form.core.types.FormController
import com.example.smartschedule.form.core.types.FormSchema
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class FormViewModel<T>(
    private val formSchema: FormSchema
) : ViewModel() {


    private val formController = FormController(formSchema)
    protected val controller: FormController get() = formController


    // State management
    private val _formState = MutableStateFlow(createInitialFormState())
    protected val formState: StateFlow<T> = _formState.asStateFlow()

    fun updateField(fieldId: String, value: Any?) {
        formController.updateField(fieldId, value)
        updateFormState()
    }

    protected fun updateStateOfForm(newState: T) {
        _formState.value = newState
    }


    fun validateForm(): Boolean {
        val isValid = formController.validateAll()
        updateFormState()
        return isValid
    }

    fun submitForm() {
        if (validateForm()) {
            val formData = formController.state.values
            onFormSubmit(formData)
        }
    }

    protected abstract fun createInitialFormState(): T
    protected abstract fun updateFormState()
    protected abstract fun onFormSubmit(data: Map<String, Any?>)

    // Helper methods
    protected fun getFieldValue(fieldId: String): String {
        if (formController.state.getValue<String>(fieldId) == null) {
            return ""
        }
        return (formController.state.getValue<String>(fieldId)).toString() ?: ""
    }

    protected fun getFieldError(fieldId: String): String? {
        return formController.state.getError(fieldId)
    }

    protected fun isFieldTouched(fieldId: String): Boolean {
        return formController.state.isTouched(fieldId)
    }

    protected fun hasErrors(): Boolean {
        return formController.state.hasErrors()
    }
}
