package ru.klimtrofimov.vkeducationstarter

import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Test
import ru.klimtrofimov.vkeducationstarter.data.AppsRepository
import ru.klimtrofimov.vkeducationstarter.data.SampleApps
import ru.klimtrofimov.vkeducationstarter.ui.apps.AppListViewModel

class AppListViewModelTest {
    @Test
    fun contentIsLoadedOnlyOnceAndKeptInState() {
        var calls = 0
        val expected = SampleApps.apps
        val repository = AppsRepository {
            calls += 1
            expected
        }
        val viewModel = AppListViewModel(repository)

        assertEquals(1, calls)
        assertSame(expected, viewModel.uiState.value.apps)
        assertSame(expected, viewModel.uiState.value.apps)
        assertEquals(1, calls)
    }
}
