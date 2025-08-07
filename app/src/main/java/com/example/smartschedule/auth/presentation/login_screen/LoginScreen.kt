package com.example.smartschedule.auth.presentation.login_screen

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartschedule.auth.domain.utils.ModelSamples
import com.example.smartschedule.auth.presentation.login_screen.components.ButtonsSection
import com.example.smartschedule.auth.presentation.login_screen.components.FieldsSection
import com.example.smartschedule.auth.presentation.login_screen.components.RememberMeCheckbox
import com.example.smartschedule.auth.presentation.login_screen.components.TitleSection

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onSuccessLogin: () -> Unit = {},
    onCancelClicked: () -> Unit = {},
) {
    var userName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Surface(
        modifier.fillMaxSize()
    ) {
        Column(
            modifier = modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            TitleSection(
                modifier.weight(1f)
            )
            FieldsSection(
                modifier.weight(1f),
                onUsernameChanged = { userName = it },
                onPasswordChanged = { password = it },
                username = userName,
                password = password,
                content = { RememberMeCheckbox() }
            )
            ButtonsSection(
                modifier.weight(1f),
//                onLoginClicked = onLoginClicked,
                onLoginClicked = {
                    if (ModelSamples.accessLogin(userName, password))
                        onSuccessLogin()
                    else
                        onCancelClicked()
                        Log.d("LoginScreen", "Login failed")

                },
                onCancelClicked = onCancelClicked
            )
        }
    }
}


@Composable
@Preview(showBackground = true)
fun LoginScreenPreview() {
    MaterialTheme {
        LoginScreen()
    }
}