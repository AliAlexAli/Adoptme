package com.example.adoptme.presentation

import android.R
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.app.NotificationManagerCompat
import com.example.adoptme.presentation.screens.BaseScreen
import com.example.adoptme.presentation.ui.theme.Theme
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    val intent: Intent = getIntent();
    val petId = intent.getStringExtra("petId")

    super.onCreate(savedInstanceState)
    initNotifications()
    setContent {
      Theme {
        BaseScreen(petId)
      }
    }
  }

  private fun initNotifications() {
    FirebaseMessaging.getInstance().isAutoInitEnabled = true
    val channel =
      NotificationChannel("Global", "Heads Up Notification", NotificationManager.IMPORTANCE_HIGH)
    getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
  }
}
