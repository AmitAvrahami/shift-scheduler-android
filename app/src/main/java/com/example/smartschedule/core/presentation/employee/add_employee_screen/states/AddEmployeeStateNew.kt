package com.example.smartschedule.core.presentation.employee.add_employee_screen.states

import com.example.smartschedule.core.domain.models.Employee
import com.example.smartschedule.core.presentation.common.UiState


//data class EmployeeFormState(
//    val uiState: UiState<Employee?> = UiState.Loading,
//    val formFields: EmployeeFormFields = EmployeeFormFields(),
//)
//
//data class EmployeeFormFields(
//    val nameField : FieldState = FieldState( ),
//    val employeeNumberField: FieldState = FieldState(filedType = FieldType.EMPLOYEE_NUMBER),
//    val emailField : FieldState= FieldState( filedType = FieldType.EMAIL),
//    val passwordField : FieldState  = FieldState( filedType = FieldType.PASSWORD),
//    val confirmPasswordField : FieldState = FieldState( filedType = FieldType.CONFIRM_PASSWORD),
//    val maxShiftsPerWeekField : FieldState = FieldState( filedType = FieldType.MAX_SHIFTS_PER_WEEK),
//    val userTypeField : FieldState = FieldState( filedType = FieldType.USER_TYPE),
//    val statusField : FieldState = FieldState( filedType = FieldType.STATUS),
//){
//    fun getFields() = listOf(
//        nameField,
//        employeeNumberField,
//        emailField,
//        passwordField,
//        confirmPasswordField,
//        maxShiftsPerWeekField,
//        userTypeField,
//        statusField,
//    )
//}
//
//data class FieldState(
//    val value: String = "",
//    val error: String? = null,
//    val isTouched: Boolean = false,
//    val isValid: Boolean = false,
//    val filedType: FieldType = FieldType.NAME
//)
//
//sealed class FieldType(){
//    object NAME : FieldType()
//    object EMPLOYEE_NUMBER : FieldType()
//    object EMAIL : FieldType()
//    object PASSWORD : FieldType()
//    object CONFIRM_PASSWORD : FieldType()
//    object MAX_SHIFTS_PER_WEEK : FieldType()
//    object USER_TYPE : FieldType()
//    object STATUS : FieldType()
//
//}
data class EmployeeFormState(
    val uiState: UiState<Employee?> = UiState.Loading,
    val formFields: EmployeeFormFields = EmployeeFormFields(),
) {
    fun hasErrors(): Boolean = formFields.getFields().any { !it.isValid && it.isTouched }
    fun canSubmit(): Boolean = formFields.getFields().all { it.isValid }
    fun getAllErrors(): List<String> = formFields.getFields().mapNotNull { it.error }
    fun getProgress(): Float {
        val totalFields = formFields.getFields().size.toFloat()
        val filledFields = formFields.getFields().count {
            it.value.isNotEmpty() && it.isValid
        }.toFloat()
        return if (totalFields > 0) filledFields / totalFields else 0f
    }
}

data class EmployeeFormFields(
    val nameField: FieldState = FieldState(fieldType = EmployeeFieldType.NAME),
    val employeeNumberField: FieldState = FieldState(fieldType = EmployeeFieldType.EMPLOYEE_NUMBER),
    val emailField: FieldState = FieldState(fieldType = EmployeeFieldType.EMAIL),
    val passwordField: FieldState = FieldState(fieldType = EmployeeFieldType.PASSWORD),
    val confirmPasswordField: FieldState = FieldState(fieldType = EmployeeFieldType.CONFIRM_PASSWORD),
    val maxShiftsPerWeekField: FieldState = FieldState(fieldType = EmployeeFieldType.MAX_SHIFTS_PER_WEEK),
    val userTypeField: FieldState = FieldState(fieldType = EmployeeFieldType.USER_TYPE),
    val statusField: FieldState = FieldState(fieldType = EmployeeFieldType.STATUS),
) {
    fun getFields() = listOf(
        nameField,
        employeeNumberField,
        emailField,
        passwordField,
        confirmPasswordField,
        maxShiftsPerWeekField,
        userTypeField,
        statusField,
    )

}

data class FieldState(
    val value: String = "",
    val error: String? = null,
    val isTouched: Boolean = false,
    val isValid: Boolean = false,
    val fieldType: EmployeeFieldType = EmployeeFieldType.NAME
)

enum class EmployeeFieldType {
    NAME,
    EMPLOYEE_NUMBER,
    EMAIL,
    PASSWORD,
    CONFIRM_PASSWORD,
    MAX_SHIFTS_PER_WEEK,
    USER_TYPE,
    STATUS
}