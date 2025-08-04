@file:Suppress("SpellCheckingInspection")

package com.example.smartschedule.core.presentation.employee.add_employee_screen

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.smartschedule.core.domain.models.Employee
import com.example.smartschedule.core.domain.models.UserStatus
import com.example.smartschedule.core.domain.models.UserType
import com.example.smartschedule.core.presentation.common.UiState
import com.example.smartschedule.core.presentation.employee.add_employee_screen.states.EmployeeFormState
import com.example.smartschedule.core.presentation.employee.add_employee_screen.states.FieldState
import com.example.smartschedule.core.presentation.employee.components.DropdownOption
import com.example.smartschedule.core.presentation.employee.components.calculatePasswordStrength
import com.example.smartschedule.core.presentation.employee.components.getUserStatusDisplayName
import com.example.smartschedule.core.presentation.employee.components.getUserTypeDisplayName
import com.example.smartschedule.core.presentation.employee.viewmodel.EmployeeFormViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEmployeeScreen(
    viewModel: EmployeeFormViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit = {},
    onEmployeeAdded: (Employee) -> Unit = {}
) {
    val state by viewModel.employeeFormState.collectAsState()
    val context = LocalContext.current

    // טיפול ב-side effects
    LaunchedEffect(state.uiState) {
        when (val uiState = state.uiState) {
            is UiState.Success -> {
                uiState.data?.let { employee ->
                    // הצגת הודעת הצלחה
                    Toast.makeText(context, "העובד ${employee.name} נוסף בהצלחה!", Toast.LENGTH_LONG).show()
                    onEmployeeAdded(employee)
                }
            }
            is UiState.Error -> {
                // הצגת הודעת שגיאה
                Toast.makeText(context, uiState.error.message, Toast.LENGTH_LONG).show()
            }
            else -> {}
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("הוספת עובד חדש") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "חזור")
                    }
                },
                actions = {
                    // כפתור איפוס
                    IconButton(
                        onClick = { viewModel.resetForm() }
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = "איפוס טופס")
                    }
                }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // Progress Bar
            FormProgressIndicator(
                progress = state.getProgress(),
                modifier = Modifier.fillMaxWidth()
            )

            // כרטיס פרטים אישיים
            PersonalDetailsCard(
                viewModel = viewModel,
                state = state
            )

            // כרטיס פרטי עבודה
            WorkDetailsCard(
                viewModel = viewModel,
                state = state
            )

            // כרטיס אבטחה
            SecurityCard(
                viewModel = viewModel,
                state = state
            )

            Spacer(modifier = Modifier.height(16.dp))

            // כפתורי פעולה
            ActionButtons(
                viewModel = viewModel,
                state = state,
                onNavigateBack = onNavigateBack
            )

            // הצגת שגיאות כלליות
            ErrorDisplay(state = state)
        }
    }
}

@Composable
private fun FormProgressIndicator(
    progress: Float,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "התקדמות מילוי הטופס",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier.fillMaxWidth(),
            color = when {
                                progress < 0.3f -> MaterialTheme.colorScheme.error
                                progress < 0.7f -> Color(0xFFFF9800) // כתום
                                else -> MaterialTheme.colorScheme.primary
                            },
            trackColor = ProgressIndicatorDefaults.linearTrackColor,
            strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
            )

            Text(
                text = "${(progress * 100).toInt()}% הושלם",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
private fun PersonalDetailsCard(
    viewModel: EmployeeFormViewModel,
    state: EmployeeFormState
) {
    ExpandableCard(
        title = "פרטים אישיים",
        icon = Icons.Default.Person,
        initiallyExpanded = true
    ){
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

            // שם מלא
            EnhancedFormTextField(
                fieldState = state.formFields.nameField,
                label = "שם מלא",
                placeholder = "הכנס שם מלא",
                leadingIcon = Icons.Default.Person,
                onValueChange = { viewModel.updateField("name", it) }
            )

            // מספר עובד
            EnhancedFormTextField(
                fieldState = state.formFields.employeeNumberField,
                label = "מספר עובד",
                placeholder = "123456",
                leadingIcon = Icons.Default.Phone,
                keyboardType = KeyboardType.Number,
                onValueChange = { viewModel.updateField("employeeNumber", it) }
            )

            // אימייל
            EnhancedFormTextField(
                fieldState = state.formFields.emailField,
                label = "כתובת אימייל",
                placeholder = "example@company.com",
                leadingIcon = Icons.Default.MailOutline,
                keyboardType = KeyboardType.Email,
                onValueChange = { viewModel.updateField("email", it) }
            )
        }
    }
}

@Composable
private fun WorkDetailsCard(
    viewModel: EmployeeFormViewModel,
    state: EmployeeFormState
) {
    ExpandableCard(
        title = "פרטי עבודה",
        icon = Icons.Default.Warning,
        initiallyExpanded = true
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

            // מקסימום משמרות בשבוע
            EnhancedFormTextField(
                fieldState = state.formFields.maxShiftsPerWeekField,
                label = "מקסימום משמרות בשבוע",
                placeholder = "7",
                leadingIcon = Icons.Default.Refresh,
                keyboardType = KeyboardType.Number,
                onValueChange = {
                    val intValue = it.toIntOrNull()
                    viewModel.updateField("maxShiftsPerWeek", intValue)
                }
            )

            // סוג משתמש
            EnhancedFormDropdown(
                fieldState = state.formFields.userTypeField,
                label = "סוג משתמש",
                options = UserType.entries.map {
                    DropdownOption(it.name, getUserTypeDisplayName(it))
                },
                leadingIcon = Icons.Default.AccountCircle,
                onValueChange = { viewModel.updateField("userType", it) }
            )

            // סטטוס
            EnhancedFormDropdown(
                fieldState = state.formFields.statusField,
                label = "סטטוס עובד",
                options = UserStatus.entries.map {
                    DropdownOption(it.name, getUserStatusDisplayName(it))
                },
                leadingIcon = Icons.Default.KeyboardArrowDown,
                onValueChange = { viewModel.updateField("status", it) }
            )
        }
    }
}

@Composable
private fun SecurityCard(
    viewModel: EmployeeFormViewModel,
    state: EmployeeFormState
) {
    ExpandableCard(
        title = "אבטחה וסיסמה",
        icon = Icons.Default.ThumbUp,
        initiallyExpanded = true
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

            // סיסמה
            EnhancedFormTextField(
                fieldState = state.formFields.passwordField,
                label = "סיסמה",
                placeholder = "הכנס סיסמה חזקה",
                leadingIcon = Icons.Default.Lock,
                isPassword = true,
                onValueChange = { viewModel.updateField("password", it) }
            )

            // אישור סיסמה
            EnhancedFormTextField(
                fieldState = state.formFields.confirmPasswordField,
                label = "אישור סיסמה",
                placeholder = "הכנס סיסמה שוב",
                leadingIcon = Icons.Default.AddCircle,
                isPassword = true,
                onValueChange = { viewModel.updateField("confirmPassword", it) }
            )

            // הנחיות סיסמה
            PasswordStrengthIndicator(
                password = state.formFields.passwordField.value
            )
        }
    }
}

@Composable
private fun ActionButtons(
    viewModel: EmployeeFormViewModel,
    state: EmployeeFormState,
    onNavigateBack: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        // כפתור ביטול
        OutlinedButton(
            onClick = onNavigateBack,
            modifier = Modifier.weight(1f),
            enabled = state.uiState !is UiState.Loading
        ) {
            Icon(Icons.Default.Edit, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("ביטול")
        }

        // כפתור שמירה
        Button(
            onClick = { viewModel.submitForm() },
            modifier = Modifier.weight(2f),
            enabled = state.canSubmit(),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (state.canSubmit()) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
                }
            )
        ) {
            if (state.uiState is UiState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp
                )
            } else {
                Icon(Icons.Default.AccountCircle, contentDescription = null)
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = if (state.uiState is UiState.Loading) "שומר..." else "שמור עובד"
            )
        }
    }
}

// 📁 core/presentation/employee/components/FormComponents.kt
@Composable
fun EnhancedFormTextField(
    fieldState: FieldState,
    label: String,
    placeholder: String = "",
    leadingIcon: ImageVector? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isPasswordVisible by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        OutlinedTextField(
            value = fieldState.value,
            onValueChange = onValueChange,
            label = { Text(label) },
            placeholder = { Text(placeholder) },
            leadingIcon = leadingIcon?.let { icon ->
                { Icon(icon, contentDescription = null) }
            },
            trailingIcon = if (isPassword) {
                {
                    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                        Icon(
                            imageVector = if (isPasswordVisible) Icons.AutoMirrored.Filled.ExitToApp else Icons.Default.ThumbUp,
                            contentDescription = if (isPasswordVisible) "הסתר סיסמה" else "הצג סיסמה"
                        )
                    }
                }
            } else null,
            isError = fieldState.error != null && fieldState.isTouched,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            visualTransformation = if (isPassword && !isPasswordVisible) {
                PasswordVisualTransformation()
            } else {
                VisualTransformation.None
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = TextFieldDefaults.colors(
               focusedTextColor  = if (fieldState.error != null && fieldState.isTouched) {
                    MaterialTheme.colorScheme.error
                } else {
                    MaterialTheme.colorScheme.primary
                },
                unfocusedTextColor = if (fieldState.error != null && fieldState.isTouched) {
                    MaterialTheme.colorScheme.error
                } else {
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
                }
            )

            )

        // הצגת שגיאה
        AnimatedVisibility(
            visible = fieldState.error != null && fieldState.isTouched,
            enter = slideInVertically() + fadeIn(),
            exit = slideOutVertically() + fadeOut()
        ) {
            Row(
                modifier = Modifier.padding(start = 16.dp, top = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.ThumbUp,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = fieldState.error ?: "",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }

        // הצגת הצלחה (כשהשדה תקין ונגע)
        AnimatedVisibility(
            visible = fieldState.isValid && fieldState.isTouched && fieldState.value.isNotEmpty(),
            enter = slideInVertically() + fadeIn(),
            exit = slideOutVertically() + fadeOut()
        ) {
            Row(
                modifier = Modifier.padding(start = 16.dp, top = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = Color(0xFF4CAF50),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "תקין",
                    color = Color(0xFF4CAF50),
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }
    }
}

@Composable
fun EnhancedFormDropdown(
    fieldState: FieldState,
    label: String,
    options: List<DropdownOption>,
    leadingIcon: ImageVector? = null,
    onValueChange: (String) -> Unit,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedOption = options.find { it.value == fieldState.value }

    Column(modifier = modifier) {
        Box {
            OutlinedTextField(
                value = selectedOption?.displayName ?: "",
                onValueChange = { },
                readOnly = true,
                label = { Text(label) },
                leadingIcon = leadingIcon?.let { icon ->
                    { Icon(icon, contentDescription = null) }
                },
                trailingIcon = {
                    IconButton(onClick = { expanded = true }) {
                        Icon(
                            imageVector = if (expanded) Icons.Default.ThumbUp else Icons.Default.ThumbUp,
                            contentDescription = null
                        )
                    }
                },
                isError = fieldState.error != null && fieldState.isTouched,
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = if (fieldState.error != null && fieldState.isTouched) {
                        MaterialTheme.colorScheme.error
                    } else {
                        MaterialTheme.colorScheme.primary
                    }
                )
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        onClick = { onValueChange(option.value);expanded = false },
                        text = { Text(option.displayName) }
                    )
                }
            }
        }

        // הצגת שגיאה
        AnimatedVisibility(
            visible = fieldState.error != null && fieldState.isTouched
        ) {
            Row(
                modifier = Modifier.padding(start = 16.dp, top = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = fieldState.error ?: "",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }
    }
}

@Composable
fun ExpandableCard(
    title: String,
    icon: ImageVector,
    initiallyExpanded: Boolean = false,
    content: @Composable ColumnScope.() -> Unit
) {
    var expanded by remember { mutableStateOf(initiallyExpanded) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )

                Icon(
                    imageVector = if (expanded) Icons.Default.ThumbUp else Icons.Default.ThumbUp,
                    contentDescription = null
                )
            }

            // Content
            AnimatedVisibility(
                visible = expanded,
                enter = slideInVertically() + expandVertically() + fadeIn(),
                exit = slideOutVertically() + shrinkVertically() + fadeOut()
            ) {
                Column(
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                    content = content
                )
            }
        }
    }
}

@Composable
fun PasswordStrengthIndicator(
    password: String,
    modifier: Modifier = Modifier
) {
    val strength = calculatePasswordStrength(password)

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = "חוזק הסיסמה",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
            progress = { strength.progress },
            modifier = Modifier.fillMaxWidth(),
            color = strength.color,
            trackColor = ProgressIndicatorDefaults.linearTrackColor,
            strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = strength.description,
                style = MaterialTheme.typography.titleSmall,
                color = strength.color
            )

            if (password.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))

                strength.requirements.forEach { requirement ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (requirement.isMet) Icons.Default.Edit else Icons.Default.KeyboardArrowDown,
                            contentDescription = null,
                            tint = if (requirement.isMet) Color(0xFF4CAF50) else MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = requirement.text,
                            style = MaterialTheme.typography.titleSmall,
                            color = if (requirement.isMet) {
                                Color(0xFF4CAF50)
                            } else {
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ErrorDisplay(state: EmployeeFormState) {
    AnimatedVisibility(
        visible = state.uiState is UiState.Error,
        enter = slideInVertically() + fadeIn(),
        exit = slideOutVertically() + fadeOut()
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                MaterialTheme.colorScheme.error.copy(alpha = 0.1f),
                        contentColor = MaterialTheme.colorScheme.error),

        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.ThumbUp,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "שגיאה בשמירת העובד",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    if (state.uiState is UiState.Error) {
                        Text(
                            text = state.uiState.error.message,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }
    }
}



