package com.example.adoptme.domain.model.util

import androidx.compose.runtime.LaunchedEffect
import androidx.datastore.preferences.preferencesDataStore
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
    LoginScreen(navController,
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
    RegScreen(navController, loginViewModel)
  }
}

fun mainPage(builder: NavGraphBuilder, navController: NavHostController, petsViewModel: PetsViewModel, loginViewModel: AuthViewModel) {
  builder.composable(route = NavigationEnum.Main.name) {
    loginViewModel.setError("")
    LaunchedEffect(Unit) {
      petsViewModel.getPets()
    }
    MainScreen(navController,petsViewModel, loginViewModel)
  }
}

fun petPage(builder: NavGraphBuilder, navController: NavHostController, petsViewModel: PetsViewModel, viewModel: AuthViewModel) {
  builder.composable(route = NavigationEnum.Pet.name) {
    viewModel.setError("")
    LaunchedEffect(Unit) {
      petsViewModel.getPetById(viewModel.petId.value);
    }
    PetScreen(petsViewModel, viewModel)
  }
}
