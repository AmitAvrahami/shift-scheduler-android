package com.example.smartschedule.auth.presentation.login_screen.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun LoginButton (
    modifier : Modifier = Modifier,
    onClick: () -> Unit ={}
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(horizontal = 16.dp),

        colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
    ) {
        Text(text = "Login", color = Color.White)
    }
}

@Composable
fun CancelButton (
    modifier : Modifier = Modifier,
    onClick: () -> Unit ={},
) {
    Button(
        onClick = onClick,
        modifier = modifier.padding(horizontal = 16.dp)
    ) {
        Text(text = "Cancel")
    }
}

@Composable
fun ButtonsSection(
    modifier : Modifier = Modifier,
    onLoginClicked: () -> Unit = {},
    onCancelClicked: () -> Unit = {},
) {
    Row(
        modifier = modifier.fillMaxWidth()
    ) {
        LoginButton(
            modifier = Modifier.weight(1f),
            onClick = onLoginClicked
        )
        CancelButton(
            modifier = Modifier.weight(1f),
            onClick = onCancelClicked
        )
    }
}

@Composable
@Preview(showBackground = true)
fun LoginButtonPreview(){
    LoginButton()
}