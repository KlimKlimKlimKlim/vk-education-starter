package ru.klimtrofimov.vkeducationstarter

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.lifecycle.ViewModelProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertFalse
import org.junit.Assert.assertSame
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ru.klimtrofimov.vkeducationstarter.presentation.apps.AppListViewModel

@RunWith(AndroidJUnit4::class)
class HiltViewModelTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun hiltInjectsRepositoryAndRetainsViewModelAcrossRecreation() {
        lateinit var before: AppListViewModel
        composeRule.activityRule.scenario.onActivity { activity ->
            // Используется стандартная фабрика Activity, которую предоставляет Hilt.
            before = ViewModelProvider(activity)[AppListViewModel::class.java]
        }

        composeRule.waitUntil(timeoutMillis = 35_000) { !before.uiState.value.isLoading }
        assertFalse(before.uiState.value.hasError)
        assertFalse(before.uiState.value.apps.isEmpty())
        val appsBefore = before.uiState.value.apps
        composeRule.activityRule.scenario.recreate()

        composeRule.activityRule.scenario.onActivity { activity ->
            val after = ViewModelProvider(activity)[AppListViewModel::class.java]
            assertSame(before, after)
            assertSame(appsBefore, after.uiState.value.apps)
        }
    }
}
