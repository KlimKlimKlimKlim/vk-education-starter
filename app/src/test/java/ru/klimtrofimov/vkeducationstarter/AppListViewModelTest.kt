package ru.klimtrofimov.vkeducationstarter

import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test
import ru.klimtrofimov.vkeducationstarter.domain.model.AppDetails
import ru.klimtrofimov.vkeducationstarter.domain.model.AppInfo
import ru.klimtrofimov.vkeducationstarter.domain.repository.AppsRepository
import ru.klimtrofimov.vkeducationstarter.presentation.apps.AppListViewModel

class AppListViewModelTest {
    @Test
    fun contentIsLoadedOnlyOnceAndKeptInState() {
        var calls = 0
        val expected = listOf(AppInfo("test", "Test", "Description", "Category", "test"))
        val repository = object : AppsRepository {
            override fun getApps(): List<AppInfo> {
                calls += 1
                return expected
            }
            override fun getAppDetails(id: String): AppDetails = error("Not used")
        }
        val viewModel = AppListViewModel(repository)

        assertEquals(1, calls)
        assertSame(expected, viewModel.uiState.value.apps)
        viewModel.onToolbarClick()
        assertSame(expected, viewModel.uiState.value.apps)
        assertEquals(1, calls)
        assertTrue(viewModel.effects.replayCache.isEmpty())
    }
}
