package com.example.adoptme.view

import android.annotation.SuppressLint
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.adoptme.model.util.*
import com.example.adoptme.viewmodel.AuthViewModel
import kotlinx.coroutines.launch

@Composable
fun NavigateBetweenScreen(
  navController: NavHostController,
  loginViewModel: AuthViewModel = hiltViewModel()
) {
  val startDestination =
    if (loginViewModel.isLoggedIn.value) NavigationEnum.Welcome.name else NavigationEnum.Login.name

  NavHost(navController = navController, startDestination = startDestination) {
    loginPage(this, navController, loginViewModel)
    registerPage(this, navController, loginViewModel)
    welcomePage(this, loginViewModel)
  }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun BaseScreen(viewModel: AuthViewModel = hiltViewModel()) {
  val navController = rememberNavController()
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
        MainTopBar(currentScreen)
    },
    drawerContent = {
      if (currentScreen == NavigationEnum.Register
        || currentScreen == NavigationEnum.Login
      ) {
        AuthDrawerContent(navController = navController, scope = scope, scaffoldState = scaffoldState)
      }
    }
  ) {
    NavigateBetweenScreen(navController)
  }
}
