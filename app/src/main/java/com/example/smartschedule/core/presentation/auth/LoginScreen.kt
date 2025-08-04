package com.example.smartschedule.core.presentation.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.smartschedule.core.domain.models.User
import com.example.smartschedule.core.presentation.viewmodel.LoginViewModel


@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onLoginSuccess: (User) -> Unit = {},
    viewModel: LoginViewModel = hiltViewModel()
    ){
    //Input Login Vars
    var username by remember { mutableStateOf("amit@admin.com") } //TODO : Delete IT
    var password by remember { mutableStateOf("123456") }

    //Login State Vars
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessages by viewModel.errorMessage.collectAsState()

    //Execute vars
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("מסך התחברות")

        OutlinedTextField(
            value = username,
            onValueChange = {
                username = it
                viewModel.clearError()
            },
            label = { Text("שם משתמש") },
            modifier = Modifier.fillMaxWidth()
            )

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                viewModel.clearError()
            },
            label = { Text("סיסמה") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(16.dp))

        errorMessages?.let { errorMessage ->
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        Button(
            onClick = {
                if (username.isNotBlank() && password.isNotBlank()) {
                    viewModel.login(username, password, onLoginSuccess)
                } else {
                    // TODO: Handle empty fields
                }
            },
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isLoading) {
                Text("מתחבר...")
            }
            else {
                Text("התחבר")
            }
        }
    }
}






@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen()
}