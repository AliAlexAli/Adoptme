package com.example.adoptme.model.util

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.adoptme.view.LoginScreen
import com.example.adoptme.view.RegScreen
import com.example.adoptme.view.WelcomeScreen
import com.example.adoptme.viewmodel.AuthViewModel

fun loginPage(
  builder: NavGraphBuilder,
  navController: NavHostController,
  loginViewModel: AuthViewModel
) {
  builder.composable(route = NavigationEnum.Login.name) {
    loginViewModel.setError("")
    LoginScreen(
      viewModel = loginViewModel
    )
  }
}

fun registerPage(
  builder: NavGraphBuilder,
  navController: NavHostController,
  loginViewModel: AuthViewModel
) {
  builder.composable(route = NavigationEnum.Register.name) {
    loginViewModel.setError("")
    RegScreen(loginViewModel)
  }
}

fun welcomePage(builder: NavGraphBuilder, loginViewModel: AuthViewModel) {
  builder.composable(route = NavigationEnum.Welcome.name) {
    WelcomeScreen(loginViewModel)
  }
}
