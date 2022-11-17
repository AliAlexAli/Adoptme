package com.example.adoptme.presentation.screens.components

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.adoptme.domain.model.util.NavigationEnum
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun AuthTopBar(
  currentScreen: NavigationEnum, scope: CoroutineScope, scaffoldState: ScaffoldState
) {
  TopAppBar(
    title = { Text(text = stringResource(currentScreen.title)) },

    navigationIcon = {
      IconButton(onClick = {
        scope.launch {
          scaffoldState.drawerState.open()
        }
      }) {
        Icon(Icons.Default.Menu, "Menu Icon")
      }
    }
  )
}
