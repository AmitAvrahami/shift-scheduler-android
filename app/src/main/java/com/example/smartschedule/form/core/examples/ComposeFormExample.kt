//package com.example.smartschedule.form.core.examples
//
//import ads_mobile_sdk.h5
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ArrowDropDown
//import androidx.compose.material3.Button
//import androidx.compose.material3.Card
//import androidx.compose.material3.CardDefaults
//import androidx.compose.material3.Checkbox
//import androidx.compose.material3.CheckboxDefaults
//import androidx.compose.material3.DropdownMenu
//import androidx.compose.material3.DropdownMenuItem
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.OutlinedTextField
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.derivedStateOf
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.input.KeyboardType
//import androidx.compose.ui.unit.dp
//import com.example.smartschedule.form.core.constants.FieldType
//import com.example.smartschedule.form.core.dsl.form
//import com.example.smartschedule.form.core.types.FormController
//
//// 📁 examples/ComposeFormExample.kt
//@Composable
//fun UserRegistrationScreen() {
//    // יצירת הסכמה
//    val schema = remember {
//        form("user-registration") {
//            field<String>("fullName", FieldType.TEXT, required = true) {
//                minLength(2)
//                maxLength(50)
//            }
//
//            field<String>("email", FieldType.EMAIL, required = true) {
//                email()
//            }
//
//            field<String>("phone", FieldType.PHONE) {
//                israeliPhone()
//            }
//
//            field<String>("idNumber", FieldType.TEXT, required = true) {
//                israeliId()
//            }
//
//            field<Int>("age", FieldType.NUMBER) {
//                minValue(18.0)
//                maxValue(120.0)
//            }
//        }
//    }
//
//    // בקר הטופס
//    val controller = remember { FormController(schema) }
//    val state by remember { derivedStateOf { controller.state } }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        verticalArrangement = Arrangement.spacedBy(16.dp)
//    ) {
//        Text(
//            text = "רישום משתמש",
//            style = MaterialTheme.typography.titleMedium,
//            fontWeight = FontWeight.Bold
//        )
//
//        // שם מלא
//        FormTextField(
//            controller = controller,
//            fieldId = "fullName",
//            label = "שם מלא",
//            placeholder = "הכנס שם מלא"
//        )
//
//        // אימייל
//        FormTextField(
//            controller = controller,
//            fieldId = "email",
//            label = "כתובת אימייל",
//            placeholder = "example@email.com",
//            keyboardType = KeyboardType.Email
//        )
//
//        // טלפון
//        FormTextField(
//            controller = controller,
//            fieldId = "phone",
//            label = "מספר טלפון",
//            placeholder = "050-1234567",
//            keyboardType = KeyboardType.Phone
//        )
//
//        // תעודת זהות
//        FormTextField(
//            controller = controller,
//            fieldId = "idNumber",
//            label = "תעודת זהות",
//            placeholder = "123456789",
//            keyboardType = KeyboardType.Number
//        )
//
//        // גיל
//        FormTextField(
//            controller = controller,
//            fieldId = "age",
//            label = "גיל",
//            placeholder = "25",
//            keyboardType = KeyboardType.Number
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // כפתור שליחה
//        Button(
//            onClick = {
//                try {
//                    val formData = controller.submit()
//                    // טיפול בנתונים שנשלחו
//                    println("Form submitted: $formData")
//                } catch (e: IllegalStateException) {
//                    // יש שגיאות בטופס
//                    println("Form has errors")
//                }
//            },
//            modifier = Modifier.fillMaxWidth(),
//            enabled = !state.hasErrors() // אפשר רק אם אין שגיאות
//        ) {
//            Text("שלח")
//        }
//
//        // הצגת שגיאות כלליות
//        if (state.hasErrors()) {
//            Card(
//                modifier = Modifier.fillMaxWidth(),
//                colors =  CardDefaults.cardColors()
//            ) {
//                Text(
//                    text = "יש לתקן את השגיאות בטופס",
//                    modifier = Modifier.padding(12.dp),
//                    color = MaterialTheme.colorScheme.error,
//                    style = MaterialTheme.typography.bodyMedium
//                )
//            }
//        }
//    }
//}
//
//@Composable
//fun FormTextField(
//    controller: FormController,
//    fieldId: String,
//    label: String,
//    placeholder: String = "",
//    keyboardType: KeyboardType = KeyboardType.Text
//) {
//    val value = controller.state.getValue<String>(fieldId) ?: ""
//    val error = controller.state.getError(fieldId)
//    val isTouched = controller.state.isTouched(fieldId)
//    val isError = error != null && isTouched
//
//    Column {
//        OutlinedTextField(
//            value = value,
//            onValueChange = { controller.updateField(fieldId, it) },
//            label = { Text(label) },
//            placeholder = { Text(placeholder) },
//            isError = isError,
//            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
//            modifier = Modifier.fillMaxWidth(),
//            singleLine = true
//        )
//
//        // הצגת שגיאה
//        if (isError) {
//            Text(
//                text = error ?: "",
//                color = MaterialTheme.colorScheme.error,
//                style = MaterialTheme.typography.labelSmall,
//                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
//            )
//        }
//    }
//}
//
//// 📁 examples/AdvancedFormExample.kt
//@Composable
//fun AdvancedFormExample() {
//    val schema = remember {
//        form("advanced-form") {
//            field<String>("companyName", FieldType.TEXT, required = true) {
//                minLength(2)
//                custom("שם החברה חייב להכיל לפחות מילה אחת") { name ->
//                    name?.trim()?.contains(" ") == true
//                }
//            }
//
//            field<String>("website", FieldType.TEXT) {
//                custom("כתובת אתר לא תקינה") { url ->
//                    if (url.isNullOrBlank()) return@custom true
//                    url.startsWith("http://") || url.startsWith("https://")
//                }
//            }
//
//            field<String>("businessType", FieldType.SELECT, required = true)
//
//            field<Boolean>("agreeToTerms", FieldType.CHECKBOX, required = true) {
//                custom("חובה להסכים לתנאי השימוש") { agreed ->
//                    agreed == true
//                }
//            }
//        }
//    }
//
//    val controller = remember { FormController(schema) }
//    val businessTypes = listOf("טכנולוגיה", "מסחר", "שירותים", "תעשייה", "אחר")
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        verticalArrangement = Arrangement.spacedBy(16.dp)
//    ) {
//        Text("רישום עסק", style = MaterialTheme.typography.titleMedium)
//
//        FormTextField(
//            controller = controller,
//            fieldId = "companyName",
//            label = "שם החברה",
//            placeholder = "הכנס שם החברה"
//        )
//
//        FormTextField(
//            controller = controller,
//            fieldId = "website",
//            label = "אתר אינטרנט",
//            placeholder = "https://example.com"
//        )
//
//        // Dropdown לסוג עסק
//        FormDropdown(
//            controller = controller,
//            fieldId = "businessType",
//            label = "סוג העסק",
//            options = businessTypes
//        )
//
//        // Checkbox
//        FormCheckbox(
//            controller = controller,
//            fieldId = "agreeToTerms",
//            label = "אני מסכים לתנאי השימוש"
//        )
//
//        Button(
//            onClick = {
//                try {
//                    val data = controller.submit()
//                    println("Business registered: $data")
//                } catch (e: IllegalStateException) {
//                    println("Form validation failed")
//                }
//            },
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Text("רשום עסק")
//        }
//    }
//}
//
//@Composable
//fun FormDropdown(
//    controller: FormController,
//    fieldId: String,
//    label: String,
//    options: List<String>
//) {
//    var expanded by remember { mutableStateOf(false) }
//    val value = controller.state.getValue<String>(fieldId) ?: ""
//    val error = controller.state.getError(fieldId)
//    val isError = error != null && controller.state.isTouched(fieldId)
//
//    Column {
//        Box {
//            OutlinedTextField(
//                value = value,
//                onValueChange = { },
//                readOnly = true,
//                label = { Text(label) },
//                trailingIcon = {
//                    IconButton(onClick = { expanded = true }) {
//                        Icon(Icons.Default.ArrowDropDown, contentDescription = null)
//                    }
//                },
//                isError = isError,
//                modifier = Modifier.fillMaxWidth()
//            )
//
//            DropdownMenu(
//                expanded = expanded,
//                onDismissRequest = { expanded = false }
//            ) {
//                options.forEach { option ->
//                    DropdownMenuItem(
//                        onClick = { controller.updateField(fieldId, option); expanded = false },
//                        text = { Text(option) }
//                    )
//                    }
//                }
//            }
//        }
//
//        if (isError) {
//            Text(
//                text = error ?: "",
//                color = MaterialTheme.colorScheme.error,
//                style = MaterialTheme.typography.labelSmall,
//                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
//            )
//        }
//    }
//
//
//@Composable
//fun FormCheckbox(
//    controller: FormController,
//    fieldId: String,
//    label: String
//) {
//    val checked = controller.state.getValue<Boolean>(fieldId) ?: false
//    val error = controller.state.getError(fieldId)
//    val isError = error != null && controller.state.isTouched(fieldId)
//
//    Column {
//        Row(
//            verticalAlignment = Alignment.CenterVertically,
//            modifier = Modifier.clickable {
//                controller.updateField(fieldId, !checked)
//            }
//        ) {
//            Checkbox(
//                checked = checked,
//                onCheckedChange = { controller.updateField(fieldId, it) },
//                colors = CheckboxDefaults.colors(
//                    checkedColor = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
//                )
//            )
//
//            Text(
//                text = label,
//                modifier = Modifier.padding(start = 8.dp),
//                color = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
//            )
//        }
//
//        if (isError) {
//            Text(
//                text = error ?: "",
//                color = MaterialTheme.colorScheme.error,
//                style = MaterialTheme.typography.labelSmall,
//                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
//            )
//        }
//    }
//}
//
//// 📁 examples/SimpleLoginForm.kt - דוגמה פשוטה מאוד
//@Composable
//fun SimpleLoginForm() {
//    val schema = remember {
//        form("login") {
//            field<String>("email", FieldType.EMAIL, required = true) {
//                email()
//            }
//            field<String>("password", FieldType.PASSWORD, required = true) {
//                minLength(6)
//            }
//        }
//    }
//
//    val controller = remember { FormController(schema) }
//
//    Column(
//        modifier = Modifier.padding(16.dp),
//        verticalArrangement = Arrangement.spacedBy(16.dp)
//    ) {
//        FormTextField(controller, "email", "אימייל")
//        FormTextField(controller, "password", "סיסמה")
//
//        Button(
//            onClick = {
//                if (controller.validateAll()) {
//                    // התחברות מוצלחת
//                    val data = controller.state.values
//                    println("Login: ${data["email"]}")
//                }
//            },
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Text("התחבר")
//        }
//    }
//}