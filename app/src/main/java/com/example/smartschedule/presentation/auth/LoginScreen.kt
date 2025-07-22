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
import com.example.smartschedule.domain.common.AuthenticationResult
import com.example.smartschedule.domain.repository.AuthenticationRepository
import com.example.smartschedule.domain.usecase.LoginUseCase
import kotlinx.coroutines.launch


@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onLoginSuccess: (String) -> Unit = {},
    loginUseCase: LoginUseCase? = null,
    // TODO: Replace with ViewModel pattern in future sprint
    // LoginScreen shouldn't know about Repository directly
    authRepository: AuthenticationRepository? = null,
){
    //Input Login Vars
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    //Login State Vars
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    //Execute vars
    val coroutineScope = rememberCoroutineScope()

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
                if (username.isNotBlank() && password.isNotBlank()) {
                    isLoading = true
                    coroutineScope.launch {
                        val result = loginUseCase?.execute(username, password)
                            ?: authRepository?.let { repo ->
                                val user = repo.login(username, password)
                                if (user != null) AuthenticationResult.Success(user)
                                else AuthenticationResult.Error("שם משתמש או סיסמה שגויים")
                            } ?: AuthenticationResult.Error("אין UseCase זמין")

                        when (result) {
                            is AuthenticationResult.Success -> {
                                if (result.user != null) {
                                    Log.d("LoginScreen", "✅ משתמש מאומת: ${result.user.type}")
                                    errorMessage = null
                                    onLoginSuccess(result.user.type.name.lowercase())
                                } else {
                                    Log.d("LoginScreen", "⚠️ Success ללא user")
                                    errorMessage = "שגיאה לא צפויה"
                                    isLoading = false
                                }
                            }

                            is AuthenticationResult.Error -> {
                                isLoading = false
                                errorMessage = result.message
                                Log.d("LoginScreen", "❌ שגיאה: ${result.message}")
                            }

                            is AuthenticationResult.Loading -> Unit
                        }
                    }
                }else {
                            errorMessage = "יש למלא את כל השדות"
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