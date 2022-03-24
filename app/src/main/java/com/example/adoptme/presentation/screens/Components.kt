package com.example.adoptme.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.adoptme.R
import com.example.adoptme.domain.model.util.NavigationEnum
import com.example.adoptme.presentation.AuthViewModel
import com.example.adoptme.presentation.PetsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.*


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
  currentScreen: NavigationEnum, scope: CoroutineScope, scaffoldState: ScaffoldState
) {
  TopAppBar(
    title = { Text(text = stringResource(currentScreen.title)) },

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
      Text(
        text = stringResource(id = R.string.register),
        textAlign = TextAlign.Start,
        modifier = Modifier.fillMaxWidth()
      )
    }
    TextButton(modifier = Modifier.fillMaxWidth(), onClick = {
      navController.navigate(NavigationEnum.Login.name)
      scope.launch {
        scaffoldState.drawerState.close()
      }
    }) {
      Text(
        text = stringResource(id = R.string.login),
        textAlign = TextAlign.Start,
        modifier = Modifier.fillMaxWidth()
      )
    }
  }
}

@Composable
fun MainDrawerContent(
  navController: NavController,
  scope: CoroutineScope,
  scaffoldState: ScaffoldState,
  viewModel: AuthViewModel
) {
  Column(
    Modifier
      .padding(20.dp)
      .fillMaxSize(), Arrangement.spacedBy(20.dp)
  ) {
    TextButton(modifier = Modifier.fillMaxWidth(),
      onClick = {
        viewModel.signOut()
        navController.navigate(NavigationEnum.Login.name)
        scope.launch {
          scaffoldState.drawerState.close()
        }
      }) {
      Text(
        text = stringResource(id = R.string.exit),
        textAlign = TextAlign.Start,
        modifier = Modifier.fillMaxWidth()
      )
    }
  }
}

@Composable
fun AddPetFloatingActionButton(viewModel: PetsViewModel) {
  FloatingActionButton(
    onClick = {
      viewModel.showAddPet.value = true
    }
  ) {
    Icon(
      imageVector = Icons.Default.Add,
      contentDescription = "Add new pet"
    )
  }
}

@Composable
fun AddPetDialog(viewModel: PetsViewModel) {
  var name by remember { mutableStateOf("") }
  var birth by remember { mutableStateOf("") }
  var sex by remember { mutableStateOf("") }
  var size by remember { mutableStateOf("") }
  var description by remember { mutableStateOf("") }
  var image by remember { mutableStateOf("") }
  var title by remember { mutableStateOf("") }
  var author by remember { mutableStateOf("") }
  val focusRequester = FocusRequester()

  AlertDialog(
    onDismissRequest = {
      viewModel.showAddPet.value = false
    },
    text = {
      Column {
        TextField(
          value = name,
          onValueChange = { name = it },
          placeholder = {
            Text(
              text = "Név"
            )
          },
          modifier = Modifier.focusRequester(focusRequester)
        )
        DisposableEffect(Unit) {
          focusRequester.requestFocus()
          onDispose { }
        }
        Spacer(
          modifier = Modifier.height(16.dp)
        )
        TextField(
          value = sex,
          onValueChange = { sex = it },
          placeholder = {
            Text(
              text = "Nem"
            )
          },
          modifier = Modifier.focusRequester(focusRequester)
        )
        DisposableEffect(Unit) {
          focusRequester.requestFocus()
          onDispose { }
        }
        Spacer(
          modifier = Modifier.height(16.dp)
        )
        TextField(
          value = description,
          onValueChange = { description = it },
          placeholder = {
            Text(
              text = "Leírás"
            )
          },
          modifier = Modifier.focusRequester(focusRequester)
        )
        DisposableEffect(Unit) {
          focusRequester.requestFocus()
          onDispose { }
        }
      }
    },
    confirmButton = {
      TextButton(
        onClick = {
          viewModel.showAddPet.value = false
          viewModel.addPet(
            name,
            Calendar.getInstance().time,
            sex,
            "Közepes",
            description,
            "https://www.crossdogs.org/images/dog-placeholder.png"
          )
        }
      ) {
        Text(
          text = "Hozzáadás"
        )
      }
    },
    dismissButton = {
      TextButton(
        onClick = {
          viewModel.showAddPet.value = false
        }
      ) {
        Text(
          text = "Mégse"
        )
      }
    }
  )
}
