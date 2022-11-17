package com.example.adoptme.presentation.screens.components

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.example.adoptme.R
import com.example.adoptme.presentation.AuthViewModel

@Composable
fun PasswordField(focusManager: FocusManager, viewModel: AuthViewModel) {
  val password = viewModel.password.value
  var showPassword by remember { mutableStateOf(false) }

  OutlinedTextField(
    value = password,
    label = { Text(text = stringResource(id = R.string.password)) },
    singleLine = true,
    onValueChange = { viewModel.setPassword(it) },

    keyboardOptions = KeyboardOptions(
      keyboardType = KeyboardType.Password,
      imeAction = ImeAction.Done
    ),
    keyboardActions = KeyboardActions(
      onDone = {
        focusManager.clearFocus()
      }
    ),
    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
    trailingIcon = {
      val image = if (showPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff

      IconButton(onClick = { showPassword = !showPassword }) {
        Icon(imageVector = image, contentDescription = "Toggle password visibility icon")
      }
    }

  )
}
