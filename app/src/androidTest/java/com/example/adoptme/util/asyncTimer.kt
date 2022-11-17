package com.example.adoptme.util

import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.example.adoptme.presentation.MainActivity
import java.util.*
import kotlin.concurrent.schedule

fun asyncTimer(
  composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>,
  delay: Long = 1000
) {
  AsyncTimer.start(delay)
  composeTestRule.waitUntil(
    condition = { AsyncTimer.expired },
    timeoutMillis = delay + 1000
  )
}

object AsyncTimer {
  var expired = false
  fun start(delay: Long) {
    expired = false
    Timer().schedule(delay) {
      expired = true
    }
  }
}
