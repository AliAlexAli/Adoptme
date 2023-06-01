package com.example.adoptme.presentation.screens.components

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import com.example.adoptme.R
import com.example.adoptme.presentation.AuthViewModel

@Composable
fun UpdateUserButton(viewModel: AuthViewModel) {
  val saved = remember { mutableStateOf(false) }
  Button(
    enabled = viewModel.isValidEmailAndPassword(),
    content = { Text(text = stringResource(id = R.string.modify)) },
    onClick = {
      viewModel.updateUser()
      saved.value = true
    }
  )
  if (saved.value) {
    saved.value = false
    viewModel.setError(stringResource(R.string.data_saved))
  }
}
