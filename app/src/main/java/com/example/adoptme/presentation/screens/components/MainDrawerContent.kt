package com.example.adoptme.presentation.screens.components

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.adoptme.R
import com.example.adoptme.domain.model.util.NavigationEnum
import com.example.adoptme.presentation.AuthViewModel
import com.example.adoptme.presentation.PetsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun MainDrawerContent(
  navController: NavController,
  scope: CoroutineScope,
  scaffoldState: ScaffoldState,
  authViewModel: AuthViewModel,
  petsViewModel: PetsViewModel
) {
  Column(
    Modifier
      .padding(20.dp)
      .fillMaxSize(), Arrangement.spacedBy(20.dp)
  ) {
    TextButton(modifier = Modifier.fillMaxWidth(),
      onClick = {
        navController.navigate(NavigationEnum.Main.name)
        scope.launch {
          petsViewModel.clearFilters()
          scaffoldState.drawerState.close()
        }
      }) {
      Text(
        text = stringResource(id = R.string.pets),
        textAlign = TextAlign.Start,
        modifier = Modifier.fillMaxWidth()
      )
    }
    TextButton(modifier = Modifier.fillMaxWidth(),
      onClick = {
        navController.navigate(NavigationEnum.MyData.name)
        scope.launch {
          scaffoldState.drawerState.close()
        }
      }) {
      Text(
        text = stringResource(id = R.string.myData),
        textAlign = TextAlign.Start,
        modifier = Modifier.fillMaxWidth()
      )
    }
    TextButton(modifier = Modifier.fillMaxWidth(),
      onClick = {
        petsViewModel.clearFilters()
        petsViewModel.setFilterOwner(authViewModel.user.value.id)
        authViewModel.user.value.id?.let { Log.d("getPetsValues", it) }
        petsViewModel.getPets()
        navController.navigate(NavigationEnum.Main.name)
        scope.launch {
          scaffoldState.drawerState.close()
        }
      }) {
      Text(
        text = stringResource(id = R.string.myPets),
        textAlign = TextAlign.Start,
        modifier = Modifier.fillMaxWidth()
      )
    }
    TextButton(modifier = Modifier.fillMaxWidth(),
      onClick = {
        authViewModel.signOut()
        navController.navigate(NavigationEnum.Login.name)
        scope.launch {
          scaffoldState.drawerState.close()
        }
      }) {
      Text(
        text = stringResource(id = R.string.exit),
        textAlign = TextAlign.Start,
        modifier = Modifier.fillMaxWidth()
      )
    }
  }
}
