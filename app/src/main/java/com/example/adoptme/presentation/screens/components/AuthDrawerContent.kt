package com.example.adoptme.presentation.screens.components

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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun AuthDrawerContent(
  navController: NavController,
  scope: CoroutineScope,
  scaffoldState: ScaffoldState
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
        navController.navigate(NavigationEnum.Register.name)
        scope.launch {
          scaffoldState.drawerState.close()
        }
      }) {
      Text(
        text = stringResource(id = R.string.register),
        textAlign = TextAlign.Start,
        modifier = Modifier.fillMaxWidth()
      )
    }
    TextButton(modifier = Modifier.fillMaxWidth(), onClick = {
      navController.navigate(NavigationEnum.Login.name)
      scope.launch {
        scaffoldState.drawerState.close()
      }
    }) {
      Text(
        text = stringResource(id = R.string.login),
        textAlign = TextAlign.Start,
        modifier = Modifier.fillMaxWidth()
      )
    }
  }
}
