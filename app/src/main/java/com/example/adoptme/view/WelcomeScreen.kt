package com.example.adoptme.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.adoptme.viewmodel.AuthViewModel

@Composable
fun WelcomeScreen(viewModel: AuthViewModel) {
  Column(modifier = Modifier.padding(top = 8.dp)) {
    WelcomeText(viewModel)
    LogoutButton(viewModel)
  }
}

@Composable
fun WelcomeText(viewModel: AuthViewModel) {
  Text(
    text = "Hello ${viewModel.userEmail.value}",
    modifier = Modifier.fillMaxWidth(),
    textAlign = TextAlign.Center,
  )
}

@Composable
fun LogoutButton(viewModel: AuthViewModel) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(top = 40.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Button(onClick = { viewModel.signOut() }) {
      Text(text = "Kijelentkezés")
    }
  }
}
