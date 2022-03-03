package com.example.adoptme.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.adoptme.R
import com.example.adoptme.model.util.NavigationEnum
import com.example.adoptme.viewmodel.AuthViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


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

@Composable
fun ButtonEmailPasswordCreate(viewModel: AuthViewModel) {
  Button(
    enabled = viewModel.isValidEmailAndPassword(),
    content = { Text(text = stringResource(id = R.string.register)) },
    onClick = {
      viewModel.createUserWithEmailAndPassword()

    }
  )
}

@Composable
fun ButtonEmailPasswordLogin(viewModel: AuthViewModel) {
  Button(
    enabled = viewModel.isValidEmailAndPassword(),
    content = { Text(text = stringResource(R.string.login)) },
    onClick = { viewModel.signInWithEmailAndPassword() })
}

@Composable
fun AuthTopBar(
  currentScreen: NavigationEnum, scope: CoroutineScope, scaffoldState: ScaffoldState
) {
  TopAppBar(
    title = { Text(text = stringResource(currentScreen.title)) },
    // To avoid going back to previous screen after login/logout click

    navigationIcon = {
      IconButton(onClick = {
        scope.launch {
          scaffoldState.drawerState.open()
        }
      }) {
        Icon(Icons.Default.Menu, "Menu Icon")
      }
    }
  )
}

@Composable
fun MainTopBar(
  currentScreen: NavigationEnum
) {
  TopAppBar(
    title = { Text(text = stringResource(currentScreen.title)) },
  )
}

@Composable
fun AuthDrawerContent(
  navController: NavController,
  scope: CoroutineScope,
  scaffoldState: ScaffoldState
) {
  Column(
    Modifier
      .padding(20.dp)
      .fillMaxSize(), Arrangement.spacedBy(20.dp)
  ) {
    TextButton(modifier = Modifier.fillMaxWidth(),
      onClick = {
        navController.navigate(NavigationEnum.Register.name)
        scope.launch {
          scaffoldState.drawerState.close()
        }
      }) {
      Text(text = stringResource(id = R.string.register), textAlign = TextAlign.Start, modifier = Modifier.fillMaxWidth())
    }
    TextButton(modifier = Modifier.fillMaxWidth(), onClick = {
      navController.navigate(NavigationEnum.Login.name)
      scope.launch {
        scaffoldState.drawerState.close()
      }
    }) {
      Text(text = stringResource(id = R.string.login), textAlign = TextAlign.Start, modifier = Modifier.fillMaxWidth())
    }
  }
}
