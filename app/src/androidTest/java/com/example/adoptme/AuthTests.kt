package com.example.adoptme

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.adoptme.presentation.MainActivity
import com.example.adoptme.presentation.screens.BaseScreen
import com.example.adoptme.presentation.ui.theme.Theme
import com.example.adoptme.util.Constants
import com.example.adoptme.util.asyncTimer
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class AuthTests {

  @get:Rule
  val composeTestRule = createAndroidComposeRule<MainActivity>()

  @Test
  fun register() {

    composeTestRule.setContent {
      Theme {
        BaseScreen()
      }
    }

    composeTestRule.onRoot().performGesture { swipeRight() }
    composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.register))
      .performClick()
    composeTestRule.waitForIdle()
    composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.email))
      .performTextInput(Constants.email)
    composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.password))
      .performTextInput(Constants.password)
    composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.ownerName))
      .performTextInput(Constants.name)
    composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.ownerPhone))
      .performTextInput(Constants.phone)
    composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.ownerAddress))
      .performTextInput(Constants.address)
    composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.ownerSite))
      .performTextInput(Constants.site)
    composeTestRule.onAllNodesWithText(composeTestRule.activity.getString(R.string.register))
      .filterToOne(hasTestTag(composeTestRule.activity.getString(R.string.test_submit_button)))
      .performClick()

    asyncTimer(composeTestRule,3000)

    composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.register))
      .assertDoesNotExist()

    composeTestRule.onAllNodesWithTag(composeTestRule.activity.getString(R.string.test_pet_card))
      .onFirst().assertIsDisplayed()
  }

  @Test
  fun login() {

    composeTestRule.setContent {
      Theme {
        BaseScreen()
      }
    }

    composeTestRule.onRoot().performGesture { swipeRight() }
    composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.login))
      .performClick()
    composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.email))
      .performTextInput(Constants.email)
    composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.password))
      .performTextInput(Constants.password)
    composeTestRule.onAllNodesWithText(composeTestRule.activity.getString(R.string.login))
      .filterToOne(hasTestTag(composeTestRule.activity.getString(R.string.test_submit_button)))
      .performClick()

    asyncTimer(composeTestRule)

    composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.login))
      .assertDoesNotExist()
    composeTestRule.onAllNodesWithTag(composeTestRule.activity.getString(R.string.test_pet_card))
      .onFirst().assertIsDisplayed()

    asyncTimer(composeTestRule)

    composeTestRule.onRoot().performGesture { swipeRight() }
    composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.exit))
      .performClick()
  }

}
