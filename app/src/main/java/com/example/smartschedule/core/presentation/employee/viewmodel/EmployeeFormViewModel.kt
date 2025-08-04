package com.example.smartschedule.core.presentation.employee.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.smartschedule.core.domain.common.fold
import com.example.smartschedule.core.domain.models.Employee
import com.example.smartschedule.core.domain.models.UserStatus
import com.example.smartschedule.core.domain.models.UserType
import com.example.smartschedule.core.domain.usecase.AddEmployeeUseCase
import com.example.smartschedule.core.presentation.common.ErrorState
import com.example.smartschedule.core.presentation.common.UiState
import com.example.smartschedule.core.presentation.employee.add_employee_screen.states.EmployeeFieldType
import com.example.smartschedule.core.presentation.employee.add_employee_screen.states.EmployeeFormFields
import com.example.smartschedule.core.presentation.employee.add_employee_screen.states.EmployeeFormState
import com.example.smartschedule.core.presentation.employee.add_employee_screen.states.FieldState
import com.example.smartschedule.core.presentation.viewmodel.form.FormViewModel
import com.example.smartschedule.form.core.constants.FieldType
import com.example.smartschedule.form.core.dsl.form
// import com.example.smartschedule.form.core.types.FormController // Not used directly
import com.example.smartschedule.form.core.types.FormSchema
import dagger.hilt.android.lifecycle.HiltViewModel
// import kotlinx.coroutines.flow.MutableStateFlow // Not used directly
import kotlinx.coroutines.flow.StateFlow
// import kotlinx.coroutines.flow.asStateFlow // Not used directly
import kotlinx.coroutines.launch
// import timber.log.Timber // Replaced with android.util.Log
import javax.inject.Inject

@HiltViewModel
class EmployeeFormViewModel @Inject constructor(
    private val addEmployeeUseCase: AddEmployeeUseCase,
) : FormViewModel<EmployeeFormState>(createEmployeeFormSchema()){
    companion object {
        private const val TAG = "EmployeeFormViewModel"

        private fun createEmployeeFormSchema(): FormSchema {
            Log.d(TAG, "createEmployeeFormSchema נקראה")
            return form("employee-form") {
                    field<String>("name", FieldType.TEXT, required = true) {
                        minLength(2)
                        maxLength(50)
                        custom("השם חייב להכיל רק אותיות ורווחים") { name, _ ->
                            name?.all { it.isLetter() || it.isWhitespace() } ?: true
                        }
                    }

                field<String>("employeeNumber", FieldType.TEXT, required = true) {
                    custom("מספר עובד חייב להכיל 6 ספרות") { number, _ ->
                        number?.length == 6 && number.all { it.isDigit() }
                    }
                }

                field<String>("email", FieldType.EMAIL, required = true) {
                    email()
                }

                field<String>("password", FieldType.PASSWORD, required = true) {
                    minLength(8)
                    custom("הסיסמה חייבת להכיל לפחות אות גדולה, אות קטנה ומספר") { password, _ ->
                        password?.let { p ->
                            p.any { it.isUpperCase() } &&
                                    p.any { it.isLowerCase() } &&
                                    p.any { it.isDigit() }
                        } ?: false
                    }
                }

                field<String>("confirmPassword", FieldType.PASSWORD, required = true) {
                    custom("הסיסמאות אינן תואמות") { confirmPassword, context ->
                        val password = context.getValue<String>("password")
                        confirmPassword == password
                    }
                }

                field<Int>("maxShiftsPerWeek", FieldType.NUMBER, required = true) {
                    minValue(1.0)
                    maxValue(7.0)
                }

                field<String>("userType", FieldType.SELECT, required = true) {
                    custom("חובה לבחור סוג משתמש") { userType, _ ->
                        userType in listOf("ADMIN", "MANAGER", "EMPLOYEE")
                    }
                }

                field<String>("status", FieldType.SELECT, required = true) {
                    custom("חובה לבחור סטטוס") { status, _ ->
                        status in listOf("ACTIVE", "INACTIVE", "PENDING")
                    }
                }
            }
        }
    }

    val employeeFormState: StateFlow<EmployeeFormState> get() = formState

    override fun createInitialFormState(): EmployeeFormState {
        Log.d(TAG, "createInitialFormState נקראה")
        return EmployeeFormState(
            uiState = UiState.Loading,
            formFields = EmployeeFormFields()
        )
    }

    override fun updateFormState() {
        Log.d(TAG, "updateFormState נקראה")
        val currentState = formState.value
        val updatedFields = EmployeeFormFields(
            nameField = createFieldState("name", EmployeeFieldType.NAME),
            employeeNumberField = createFieldState("employeeNumber", EmployeeFieldType.EMPLOYEE_NUMBER),
            emailField = createFieldState("email", EmployeeFieldType.EMAIL),
            passwordField = createFieldState("password", EmployeeFieldType.PASSWORD),
            confirmPasswordField = createFieldState("confirmPassword", EmployeeFieldType.CONFIRM_PASSWORD),
            maxShiftsPerWeekField = createFieldState("maxShiftsPerWeek", EmployeeFieldType.MAX_SHIFTS_PER_WEEK),
            userTypeField = createFieldState("userType", EmployeeFieldType.USER_TYPE),
            statusField = createFieldState("status", EmployeeFieldType.STATUS)
        )
        val updatedState = currentState.copy(formFields = updatedFields)
        updateStateOfForm(updatedState)
// _formState.value = currentState.copy(formFields = updatedFields)
    }
    private fun createFieldState(fieldId: String, fieldType: EmployeeFieldType): FieldState {
        Log.d(TAG, "createFieldState נקראה")
        return FieldState(
            value = getFieldValue(fieldId),
            error = if (isFieldTouched(fieldId)) getFieldError(fieldId) else null,
            isTouched = isFieldTouched(fieldId),
            isValid = getFieldError(fieldId) == null,
            fieldType = fieldType
        )
    }
    override fun onFormSubmit(data: Map<String, Any?>) {
        Log.d(TAG, "onFormSubmit נקראה")
        viewModelScope.launch {
// _formState.value = _formState.value.copy(uiState = UiState.Loading)
            updateStateOfForm(employeeFormState.value.copy(uiState = UiState.Loading))

            val employee = createEmployeeFromMap(data)
            val employeePassword = data["password"] as String

            addEmployeeUseCase.executeWithResult(employee,employeePassword ).fold(
                    onSuccess = { createdEmployee ->  onSubmittedSuccess(createdEmployee)}, // Renamed variable for clarity
                    onError = { err ->  onSubmittedError(err)}
                )
        }
    }
    private fun onSubmittedSuccess(employee: Employee) {
        Log.d(TAG, "onSubmittedSuccess נקראה. Employee: $employee")
       updateStateOfForm(
           employeeFormState.value.copy(
               uiState = UiState.Success(employee)
           )
       )
    }
    private fun onSubmittedError(error: Throwable) {
        Log.e(TAG, "onSubmittedError נקראה", error) // Changed to Log.e for errors
        updateStateOfForm(
            employeeFormState.value.copy(
                uiState = UiState.Error(ErrorState.fromThrowable(error))
            )
        )
    }
    private fun createEmployeeFromMap(data: Map<String, Any?>): Employee {
        Log.d(TAG, "createEmployeeFromMap נקראה. Data: $data")
        val employee = Employee(
            id = "", // Assuming id is always present and a String. Consider safer access if not guaranteed.
            name = data["name"] as String,
            employeeNumber = data["employeeNumber"] as String,
            email = data["email"] as String,
            maxShiftPerWeek = data["maxShiftsPerWeek"] as Int,
            type = UserType.valueOf(data["userType"] as String),
            status = UserStatus.valueOf(data["status"] as String)
        )
        return employee
    }

    private fun isFieldRequired(fieldId: String): Boolean {
        Log.d(TAG, "isFieldRequired נקראה. FieldId: $fieldId")
        return controller.schema.fields[fieldId]?.required ?: false
    }


    fun resetForm() {
        Log.d(TAG, "resetForm נקראה")
        // איפוס הטופס
        listOf("name", "employeeNumber", "email", "password", "confirmPassword",
            "maxShiftsPerWeek", "userType", "status").forEach { fieldId ->
            updateField(fieldId, "") // Assuming updateField handles this type of reset
        }
    }

}
