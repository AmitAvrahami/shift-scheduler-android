package com.example.smartschedule.auth.presentation.login_screen.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.checkerframework.checker.units.qual.C

@Composable
fun  UsernameField(
     modifier : Modifier = Modifier,
     onUsernameChanged : (String) -> Unit = {},
     value : String = ""
) {
     OutlinedTextField(
          modifier = Modifier.fillMaxWidth().padding(8.dp),
          value = value,
          onValueChange = {onUsernameChanged(it)},
          leadingIcon = {
               Icon(Icons.Rounded.AccountCircle, contentDescription = "Username")
          },
          placeholder = {
               Text("Username")
          },
          singleLine = true,

          label = {
               Text("Username")
          }
     )
}


@Composable
fun  PasswordField(
     modifier : Modifier = Modifier,
     onPasswordChanged : (String) -> Unit = {},
     value : String = ""
) {
     OutlinedTextField(
          modifier = Modifier.fillMaxWidth().padding(8.dp),
          value = value,
          onValueChange = {onPasswordChanged(it)},
          leadingIcon = {
               Icon(Icons.Rounded.Info, contentDescription = "Username")
          },
          placeholder = {
               Text("Password")
          },
          singleLine = true,
          keyboardOptions = KeyboardOptions.Default.copy(
               keyboardType = KeyboardType.Password
          ),
          visualTransformation = PasswordVisualTransformation(),
          label = {
               Text("Password")
          }
     )
}

@Composable
fun FieldsSection(
     modifier : Modifier = Modifier,
     onUsernameChanged : (String) -> Unit = {},
     onPasswordChanged : (String) -> Unit = {},
     username : String = "",
     password : String = "",
     content : @Composable () -> Unit = {}
) {
     Column {
          UsernameField(
               modifier = Modifier.weight(1f),
               onUsernameChanged = onUsernameChanged,
               value = username
          )
          PasswordField(
               modifier = Modifier.weight(1f),
               onPasswordChanged = onPasswordChanged,
               value = password
          )
          content()
     }
}

@Composable
@Preview(showBackground = true)
fun  UsernameFieldPreview(){
     Column {
          UsernameField()
          PasswordField()
     }
}