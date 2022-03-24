package com.example.adoptme.domain.model.util

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.adoptme.presentation.AuthViewModel
import com.example.adoptme.presentation.screens.LoginScreen
import com.example.adoptme.presentation.screens.MainScreen
import com.example.adoptme.presentation.screens.PetScreen
import com.example.adoptme.presentation.screens.RegScreen
import com.example.adoptme.presentation.PetsViewModel

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

fun mainPage(builder: NavGraphBuilder, navController: NavHostController, petsViewModel: PetsViewModel, loginViewModel: AuthViewModel) {
  builder.composable(route = NavigationEnum.Welcome.name) {
    loginViewModel.setError("")
    petsViewModel.getPets()
    MainScreen(navController,petsViewModel, loginViewModel)
  }
}

fun petPage(builder: NavGraphBuilder, navController: NavHostController, petsViewModel: PetsViewModel, viewModel: AuthViewModel) {
  builder.composable(route = NavigationEnum.Pet.name) {
    viewModel.setError("")
    petsViewModel.getPetById(viewModel.petId.value);
    PetScreen(petsViewModel, viewModel)
  }
}
