package net.ambitious.daigoapp.android

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
class InstrumentedStartTest {

  @get:Rule
  val composeTestRule = createAndroidComposeRule<MainActivity>()

  @OptIn(ExperimentalTestApi::class)
  @Test
  fun uiTest() {
    // textbox is visible
    composeTestRule.onNodeWithText("略したい言葉").assertIsDisplayed()
    // button is visible
    composeTestRule.onNodeWithText("生成").assertIsDisplayed()
    // button is clickable
    composeTestRule.onNodeWithContentDescription("menu").let {
      it.assertIsDisplayed()
      it.performClick()
    }
    // menu is visible
    composeTestRule.waitUntil {
      composeTestRule.onNodeWithText("利用規約").let {
        it.assertIsDisplayed()
        it.performClick().fetchSemanticsNode().size.height > 0
      }
    }
    // dialog's close button is visible
    composeTestRule.waitUntil {
      composeTestRule.onNodeWithText("閉じる").assertIsDisplayed()
        .fetchSemanticsNode().size.height > 0
    }
    composeTestRule.onNodeWithText("閉じる").performClick()
    // dialog is not visible
    composeTestRule.waitUntilDoesNotExist(hasText("閉じる"))
  }
}