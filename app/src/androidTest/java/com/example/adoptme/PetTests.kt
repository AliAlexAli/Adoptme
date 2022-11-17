package com.example.adoptme

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.adoptme.presentation.MainActivity
import com.example.adoptme.presentation.screens.BaseScreen
import com.example.adoptme.presentation.ui.theme.Theme
import com.example.adoptme.util.Constants
import com.example.adoptme.util.asyncTimer
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PetTests {

  @get:Rule
  val composeTestRule = createAndroidComposeRule<MainActivity>()

  @Before
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
  }

  @Test
  fun uploadPet() {

    composeTestRule.onNodeWithTag(composeTestRule.activity.getString(R.string.test_fab_add))
      .performClick()
    composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.name))
      .performTextInput(Constants.petName)
    composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.description))
      .performTextInput(Constants.petDescription)
    composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.add)).performClick()

    asyncTimer(composeTestRule, 3000)

    while (composeTestRule.onAllNodesWithText(Constants.petName).fetchSemanticsNodes().isEmpty()) {
      composeTestRule.onRoot().performGesture { swipeUp() }
      asyncTimer(composeTestRule, 1000)
    }
    composeTestRule.onNodeWithText(Constants.petName).onParent().performScrollTo()
    composeTestRule.onNode(
      hasAnySibling(hasText(Constants.petName)) and hasText(
        composeTestRule.activity.getString(
          R.string.petMoreButton
        )
      )
    ).performClick()

    composeTestRule.onNodeWithText(Constants.petName).assertIsDisplayed()
    composeTestRule.onNodeWithText(Constants.petDescription).assertIsDisplayed()
  }


  @Test
  fun modifyPet() {

    composeTestRule.onRoot().performGesture { swipeRight() }
    composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.myPets)).performClick()
    composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.petMoreButton)).performClick()
    composeTestRule.onNodeWithText(Constants.petName).onParent().performScrollTo()
    composeTestRule.onNode(
      hasAnySibling(hasText(Constants.petName)) and hasText(
        composeTestRule.activity.getString(
          R.string.petMoreButton
        )
      )
    ).performClick()

    composeTestRule.onNode(has)

    composeTestRule.onNodeWithText(Constants.petName).assertIsDisplayed()
    composeTestRule.onNodeWithText(Constants.petDescription).assertIsDisplayed()
  }
}
