package com.example.adoptme.presentation.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
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

  if (viewModel.error.value.isNotBlank()) scope.launch {
    scaffoldState.snackbarHostState.showSnackbar(
      viewModel.error.value
    )
  }

  Scaffold(
    scaffoldState = scaffoldState,
    topBar = {
      if (currentScreen == NavigationEnum.Register
        || currentScreen == NavigationEnum.Login
      ) {
        AuthTopBar(currentScreen, scope, scaffoldState)
      } else
        MainTopBar(currentScreen, scope, scaffoldState)
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
}

