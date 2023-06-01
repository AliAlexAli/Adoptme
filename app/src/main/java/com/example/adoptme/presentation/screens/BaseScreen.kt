package com.example.adoptme.presentation.screens

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.preferences.preferencesDataStore
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.adoptme.domain.model.util.*
import com.example.adoptme.presentation.AuthViewModel
import com.example.adoptme.presentation.PetsViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore("favorites")

@Composable
fun NavigateBetweenScreen(
    navController: NavHostController, petId: String?, authViewModel: AuthViewModel = hiltViewModel()
) {
    val startDestination = NavigationEnum.Main.name
    if (!petId.isNullOrEmpty()) NavigationEnum.Pet.name
    val petsViewModel: PetsViewModel = hiltViewModel()
    if (!petId.isNullOrEmpty()) {
        authViewModel.setPetId(petId)
    }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val keys = context.dataStore.data.map {
            it.asMap().keys
        }.firstOrNull()

        val favoriteList = keys?.filter { key ->
            context.dataStore.data.map {
                it[key] == 1
            }.firstOrNull()!!
        }?.map {
            it.name
        }
        petsViewModel.updateFavoriteList(favoriteList as ArrayList<String>)
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
