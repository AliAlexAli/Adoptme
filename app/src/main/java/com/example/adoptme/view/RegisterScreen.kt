package com.example.adoptme.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.example.adoptme.viewmodel.AuthViewModel

@Composable
fun RegScreen(viewModel: AuthViewModel) {
  val focusManager = LocalFocusManager.current

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colors.background),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    EmailField(focusManager, viewModel)
    Spacer(modifier = Modifier.height(30.dp))
    PasswordField(focusManager, viewModel)
    Spacer(modifier = Modifier.height(30.dp))
    ButtonEmailPasswordCreate(viewModel)
  }
}

