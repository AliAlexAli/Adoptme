package com.example.adoptme.presentation.screens.components

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.example.adoptme.R
import com.example.adoptme.presentation.AuthViewModel

@Composable
fun SubmitRegisterButton(viewModel: AuthViewModel) {
  Button(modifier = Modifier.testTag(stringResource(R.string.test_submit_button)),
    enabled = viewModel.isValidEmailAndPassword(),
    content = { Text(text = stringResource(id = R.string.register)) },
    onClick = {
      viewModel.createUserWithEmailAndPassword()

    }
  )
}
