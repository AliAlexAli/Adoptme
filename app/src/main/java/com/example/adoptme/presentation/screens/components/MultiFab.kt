package com.example.adoptme.presentation.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

class MultiFabItem(
  val icon: ImageVector,
  val label: String,
  val onClick: () -> Unit,
  val color: Color
)

@Composable
fun MultiFab(
  fabIcon: ImageVector,
  items: List<MultiFabItem>,
  isOpen: Boolean,
  stateChanged: (isOpen: Boolean) -> Unit,
) {
  Column(horizontalAlignment = Alignment.End) {
    if (isOpen) {
      items.forEach { item ->
        FloatingActionButton(
          onClick = {
            item.onClick()
            stateChanged(false)
          },
          modifier = Modifier.background(item.color)
        ) {
          Icon(
            imageVector = item.icon,
            contentDescription = item.label
          )
        }
        Spacer(modifier = Modifier.height(20.dp))
      }
    }
    FloatingActionButton(onClick = {
      stateChanged(!isOpen)
    }) {
      Icon(
        imageVector = fabIcon,
        contentDescription = "",
      )
    }
  }
}
