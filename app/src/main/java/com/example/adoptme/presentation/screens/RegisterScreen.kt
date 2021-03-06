package com.example.adoptme.presentation.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.adoptme.domain.model.util.NavigationEnum
import com.example.adoptme.presentation.AuthViewModel
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun RegScreen(navController: NavController, viewModel: AuthViewModel) {
  val focusManager = LocalFocusManager.current
  val backstackEntry = navController.currentBackStackEntryAsState()

  val scaffoldState = rememberScaffoldState()
  val scope = rememberCoroutineScope()
  val currentScreen =
    NavigationEnum.fromRoute(backstackEntry.value?.destination?.route, viewModel.isLoggedIn)

  if(viewModel.isLoggedIn.value) LaunchedEffect(Unit){ navController.navigate(NavigationEnum.Main.name)}

  if (viewModel.error.value.isNotBlank()) scope.launch {
    scaffoldState.snackbarHostState.showSnackbar(
      viewModel.error.value
    )
  }

  Scaffold(
    scaffoldState = scaffoldState,
    topBar = {
        AuthTopBar(currentScreen, scope, scaffoldState)
    },
    drawerContent = {
      if (viewModel.isLoggedIn.value) {
        MainDrawerContent(
          navController = navController,
          scope = scope,
          scaffoldState = scaffoldState,
          viewModel = viewModel
        )

      } else {
        AuthDrawerContent(
          navController = navController,
          scope = scope,
          scaffoldState = scaffoldState
        )
      }
    }
  ) {

    Column(
      modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colors.background),
      verticalArrangement = Arrangement.SpaceEvenly,
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      EmailField(focusManager, viewModel)
      PasswordField(focusManager, viewModel)
      OutlinedTextField(
        value = viewModel.name.value,
        label = { Text(text = "N??v") },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
          keyboardType = KeyboardType.Text,
          imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
          onNext = {
            focusManager.moveFocus(FocusDirection.Down)
          }),
        onValueChange = { viewModel.setName(it) }
      )
      OutlinedTextField(
        value = viewModel.phone.value,
        label = { Text(text = "Telefonsz??m") },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
          keyboardType = KeyboardType.Phone,
          imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
          onNext = {
            focusManager.moveFocus(FocusDirection.Down)
          }),
        onValueChange = { viewModel.setPhone(it) }
      )
      OutlinedTextField(
        value = viewModel.address.value,
        label = { Text(text = "C??m") },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
          keyboardType = KeyboardType.Text,
          imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
          onNext = {
            focusManager.moveFocus(FocusDirection.Down)
          }),
        onValueChange = { viewModel.setAddress(it) }
      )
      OutlinedTextField(
        value = viewModel.website.value,
        label = { Text(text = "Weboldal") },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
          keyboardType = KeyboardType.Text,
          imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
          onNext = {
            focusManager.moveFocus(FocusDirection.Down)
          }),
        onValueChange = { viewModel.setWebsite(it) }
      )
      ButtonEmailPasswordCreate(viewModel)
    }
  }
}

