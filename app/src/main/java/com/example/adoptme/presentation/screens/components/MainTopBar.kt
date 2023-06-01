package com.example.adoptme.presentation.screens.components

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import com.example.adoptme.presentation.PetsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun MainTopBar(
    scope: CoroutineScope,
    scaffoldState: ScaffoldState,
    viewModel: PetsViewModel,
    showSearch: Boolean = false
) {
    TopAppBar(
        title = { Text(text = "") },
        navigationIcon = {
            IconButton(onClick = {
                scope.launch {
                    scaffoldState.drawerState.open()
                }
            }) {
                Icon(Icons.Default.Menu, "Menu Icon")
            }
        },
        actions = {
            if (showSearch)
                IconButton(onClick = { viewModel.showSearchDialog.value = true }) {
                    Icon(Icons.Default.Search, "Search Icon")
                }
        }
    )
}
