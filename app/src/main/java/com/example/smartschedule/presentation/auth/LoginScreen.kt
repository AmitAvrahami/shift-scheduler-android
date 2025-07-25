package com.example.smartschedule.presentation.auth

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.smartschedule.domain.models.User
import com.example.smartschedule.domain.usecase.LoginUseCase
import com.example.smartschedule.presentation.viewmodel.LoginViewModel
import com.example.smartschedule.presentation.viewmodel.UserViewModel


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