package ru.klimtrofimov.vkeducationstarter

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import ru.klimtrofimov.vkeducationstarter.presentation.apps.AppListViewModel

@OptIn(ExperimentalCoroutinesApi::class)
class AppListViewModelTest {
    private val dispatcher = StandardTestDispatcher()
    @Before fun setUp() = Dispatchers.setMain(dispatcher)
    @After fun tearDown() = Dispatchers.resetMain()

    @Test
    fun contentIsLoadedOnceAndRetainedWithoutReplayingSnackbar() = runTest(dispatcher) {
        val repository = FakeAppsRepository()
        val viewModel = AppListViewModel(repository)
        assertTrue(viewModel.uiState.value.isLoading)
        advanceUntilIdle()
        assertEquals(1, repository.calls)
        assertSame(repository.apps, viewModel.uiState.value.apps)
        assertFalse(viewModel.uiState.value.isLoading)
        viewModel.onToolbarClick()
        assertSame(repository.apps, viewModel.uiState.value.apps)
        assertEquals(1, repository.calls)
        assertTrue(viewModel.effects.replayCache.isEmpty())
    }

    @Test
    fun errorIsShownAndRetryLoadsServerContent() = runTest(dispatcher) {
        val repository = FakeAppsRepository().apply { fail = true }
        val viewModel = AppListViewModel(repository)
        viewModel.onRetryClick()
        advanceUntilIdle()
        assertTrue(viewModel.uiState.value.hasError)
        assertTrue(viewModel.uiState.value.apps.isEmpty())
        assertEquals(1, repository.calls)
        repository.fail = false
        viewModel.onRetryClick()
        advanceUntilIdle()
        assertEquals(2, repository.calls)
        assertEquals(repository.apps, viewModel.uiState.value.apps)
        assertFalse(viewModel.uiState.value.hasError)
    }
}
