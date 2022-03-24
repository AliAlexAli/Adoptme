package com.example.adoptme.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.adoptme.presentation.ui.theme.Theme
import com.example.adoptme.presentation.screens.BaseScreen
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)


    setContent {
      Theme {
        BaseScreen()
      }
    }
  }
}
