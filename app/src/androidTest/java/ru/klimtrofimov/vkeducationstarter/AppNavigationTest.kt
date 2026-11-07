package ru.klimtrofimov.vkeducationstarter

import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeLeft
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppNavigationTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun listOpensSelectedCardAndBackReturnsToList() {
        awaitCatalog()
        composeRule.onNodeWithText("QuickNote").performClick()
        composeRule.onAllNodesWithText("QuickNote").onFirst().assertIsDisplayed()
        composeRule.onNodeWithText("Установить").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("Назад").performClick()
        composeRule.onNodeWithText("Приложения").assertIsDisplayed()
    }

    @Test
    fun showcaseHasThreePagesAndClickReturnsToList() {
        awaitCatalog()
        composeRule.onNodeWithText("QuickNote").performClick()
        composeRule.onNodeWithText("Витрина").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithTag("showcase_pager").performScrollTo()
        assertShowcasePage(1)
        composeRule.onNodeWithTag("showcase_pager").performTouchInput { swipeLeft() }
        composeRule.waitForIdle()
        assertShowcasePage(2)
        composeRule.onNodeWithText("Pixtrix").assertIsDisplayed()
        composeRule.onNodeWithTag("showcase_pager").performTouchInput { swipeLeft() }
        composeRule.waitForIdle()
        assertShowcasePage(3)
        composeRule.onNodeWithTag("showcase_pager").performTouchInput { swipeLeft() }
        composeRule.waitForIdle()
        assertShowcasePage(3)
        composeRule.onNodeWithText("FlowCard", substring = false).assertIsDisplayed()
        composeRule.onNodeWithText("FlowCard", substring = false).performClick()
        composeRule.onNodeWithText("Приложения").assertIsDisplayed()
    }

    @Test
    fun contentSurvivesRecreationAndSnackbarIsNotRepeated() {
        awaitCatalog()
        composeRule.onNodeWithTag("app_list_toolbar").performClick()
        composeRule.onNodeWithText("Это список приложений").assertIsDisplayed()
        composeRule.activityRule.scenario.recreate()
        composeRule.onNodeWithTag("app_list").assertIsDisplayed()
        composeRule.onNodeWithText("QuickNote").assertIsDisplayed()
        composeRule.onNodeWithText("Это список приложений").assertDoesNotExist()
    }

    // Интеграционный smoke-test: нужен доступ к учебному серверу.
    private fun awaitCatalog() {
        composeRule.waitUntil(timeoutMillis = 35_000) {
            composeRule.onAllNodesWithTag("app_list").fetchSemanticsNodes().isNotEmpty()
        }
    }

    private fun assertShowcasePage(page: Int) {
        composeRule.onNodeWithTag("showcase_pager").assert(
            SemanticsMatcher.expectValue(
                SemanticsProperties.StateDescription,
                "Страница $page из 3",
            )
        )
    }
}
