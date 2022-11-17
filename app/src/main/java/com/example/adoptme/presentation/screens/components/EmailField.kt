package com.example.adoptme.presentation.screens.components

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.example.adoptme.R
import com.example.adoptme.presentation.AuthViewModel

@Composable
fun EmailField(focusManager: FocusManager, viewModel: AuthViewModel) {

  val userEmail = viewModel.userEmail.value

  OutlinedTextField(
    value = userEmail,
    label = { Text(text = stringResource(id = R.string.email)) },
    singleLine = true,
    keyboardOptions = KeyboardOptions(
      keyboardType = KeyboardType.Email,
      imeAction = ImeAction.Next
    ),
    keyboardActions = KeyboardActions(
      onNext = {
        focusManager.moveFocus(FocusDirection.Down)
      }),
    onValueChange = { viewModel.setUserEmail(it) }
  )
}
