package com.example.adoptme.presentation.screens.components

import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.example.adoptme.R
import com.example.adoptme.presentation.PetsViewModel


@Composable
fun AddPetFab(viewModel: PetsViewModel) {
  FloatingActionButton(
    modifier = Modifier.testTag(stringResource(R.string.test_fab_add)),
    onClick = {
      viewModel.showAddPet.value = true
    }
  ) {
    Icon(
      imageVector = Icons.Default.Add,
      contentDescription = "Add new pet"
    )
  }
}
