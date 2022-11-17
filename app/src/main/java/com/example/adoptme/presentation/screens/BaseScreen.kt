package com.example.adoptme.presentation.screens

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.datastore.preferences.preferencesDataStore
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
  petId: String?,
  authViewModel: AuthViewModel = hiltViewModel()
) {
  var startDestination =
    if (petId.isNullOrEmpty()) NavigationEnum.Main.name else NavigationEnum.Pet.name
  val petsViewModel: PetsViewModel = hiltViewModel()
  if (!petId.isNullOrEmpty()) {
    authViewModel.setpetId(petId)
  }

  NavHost(navController = navController, startDestination = startDestination) {
    loginPage(this, navController, authViewModel)
    registerPage(this, navController, authViewModel, petsViewModel)
    mainPage(this, navController, petsViewModel, authViewModel)
    petPage(this, navController, petsViewModel, authViewModel)
    myDataPage(this, navController, authViewModel, petsViewModel)
  }
}

val Context.notificationStore by preferencesDataStore("notification")

@Composable
fun BaseScreen(petId: String? = null) {
  val navController = rememberNavController()
  NavigateBetweenScreen(navController, petId)
}
