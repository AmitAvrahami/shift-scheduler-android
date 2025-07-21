package com.example.smartschedule.presentation.auth

import android.util.Log
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp


@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onLoginSuccess: (String) -> Unit = {}
){
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("מסך התחברות")

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("שם משתמש") },
            modifier = Modifier.fillMaxWidth()
            )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("סיסמה") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(16.dp))

        errorMessage?.let { errorMessage ->
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        Button(
            onClick = {
                if(username.isNotBlank() && password.isNotBlank()) {
                    val userInfo = when {
                        username == "admin" && password == "123" -> "manager"
                        username == "amit" && password == "456" -> "employee"
                        username == "manager" && password == "789" -> "manager"
                        else -> null
                    }

                    if (userInfo != null) {
                        Log.d("LoginScreen", "✅ משתמש מאומת: $userInfo")
                        errorMessage = null
                        onLoginSuccess(userInfo)
                    } else {
                        errorMessage = "שם משתמש או סיסמה שגויים"
                        Log.d("LoginScreen", "❌ פרטים שגויים!")
                    }
                } else {
                    errorMessage = "יש למלא את כל השדות"
                    Log.d("LoginScreen", "❌ שדות ריקים!")
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("התחבר")
        }
    }
}






@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen()
}