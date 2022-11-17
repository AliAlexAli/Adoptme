package com.example.adoptme.domain.model.util

import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.adoptme.presentation.AuthViewModel
import com.example.adoptme.presentation.PetsViewModel
import com.example.adoptme.presentation.screens.*

fun loginPage(
  builder: NavGraphBuilder,
  navController: NavHostController,
  loginViewModel: AuthViewModel
) {
  builder.composable(route = NavigationEnum.Login.name) {
    loginViewModel.setError("")
    LoginScreen(
      navController,
      viewModel = loginViewModel
    )
  }
}

fun registerPage(
  builder: NavGraphBuilder,
  navController: NavHostController,
  authViewModel: AuthViewModel,
  petsViewModel: PetsViewModel
) {
  builder.composable(route = NavigationEnum.Register.name) {
    authViewModel.setError("")
    RegScreen(navController, authViewModel, petsViewModel)
  }
}

fun myDataPage(
  builder: NavGraphBuilder,
  navController: NavHostController,
  authViewModel: AuthViewModel,
  petsViewModel: PetsViewModel
) {
  builder.composable(route = NavigationEnum.MyData.name) {
    authViewModel.setError("")
    MyDataScreen(navController, authViewModel, petsViewModel)
  }
}

fun mainPage(
  builder: NavGraphBuilder,
  navController: NavHostController,
  petsViewModel: PetsViewModel,
  loginViewModel: AuthViewModel
) {
  builder.composable(route = NavigationEnum.Main.name) {
    loginViewModel.setError("")
    LaunchedEffect(Unit) {
      petsViewModel.getPets()
    }
    MainScreen(navController, petsViewModel, loginViewModel)
  }
}

fun petPage(
  builder: NavGraphBuilder,
  navController: NavHostController,
  petsViewModel: PetsViewModel,
  viewModel: AuthViewModel
) {
  builder.composable(route = NavigationEnum.Pet.name) {
    viewModel.setError("")
    LaunchedEffect(Unit) {
      petsViewModel.getPetById(viewModel.petId.value)
      petsViewModel.setOwner()
    }
    PetScreen(petsViewModel, viewModel, navController)
  }
}
