package com.example.adoptme.presentation.screens

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.adoptme.domain.model.util.*
import com.example.adoptme.presentation.AuthViewModel
import com.example.adoptme.presentation.PetsViewModel

@Composable
fun NavigateBetweenScreen(
  navController: NavHostController,
  loginViewModel: AuthViewModel = hiltViewModel()
) {
  val startDestination =
    if (loginViewModel.isLoggedIn.value) NavigationEnum.Welcome.name else NavigationEnum.Login.name

  val petsViewModel: PetsViewModel = hiltViewModel()

  NavHost(navController = navController, startDestination = startDestination) {
    loginPage(this, navController, loginViewModel)
    registerPage(this, navController, loginViewModel)
    mainPage(this, navController, petsViewModel, loginViewModel)
    petPage(this, navController, petsViewModel, loginViewModel)
  }
}

@Composable
fun BaseScreen(viewModel: AuthViewModel = hiltViewModel()) {
  val navController = rememberNavController()
  NavigateBetweenScreen(navController)
}
