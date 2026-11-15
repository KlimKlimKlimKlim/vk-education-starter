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
import ru.klimtrofimov.vkeducationstarter.presentation.apps.RatingsMode
import ru.klimtrofimov.vkeducationstarter.domain.usecase.LoadRatingsUseCase

@OptIn(ExperimentalCoroutinesApi::class)
class AppListViewModelTest {
    private val dispatcher = StandardTestDispatcher()
    @Before fun setUp() = Dispatchers.setMain(dispatcher)
    @After fun tearDown() = Dispatchers.resetMain()

    @Test
    fun contentIsLoadedOnceAndRetainedWithoutReplayingSnackbar() = runTest(dispatcher) {
        val repository = FakeAppsRepository()
        val viewModel = AppListViewModel(repository, LoadRatingsUseCase(repository))
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
        val viewModel = AppListViewModel(repository, LoadRatingsUseCase(repository))
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

    @Test
    fun bothModesPublishRatingsAndIgnoreDoubleClick() = runTest(dispatcher) {
        val repository = FakeAppsRepository()
        val viewModel = AppListViewModel(repository, LoadRatingsUseCase(repository))
        advanceUntilIdle()
        viewModel.onLoadRatingsClick(RatingsMode.Sequential)
        viewModel.onLoadRatingsClick(RatingsMode.Parallel)
        assertEquals(RatingsMode.Sequential, viewModel.uiState.value.ratingsLoading)
        advanceUntilIdle()
        assertEquals(1, repository.ratingCalls)
        assertEquals(4.2f, viewModel.uiState.value.ratings.getValue("test"))
        assertTrue(viewModel.uiState.value.sequentialMillis != null)
        viewModel.onLoadRatingsClick(RatingsMode.Parallel)
        advanceUntilIdle()
        assertEquals(2, repository.ratingCalls)
        assertTrue(viewModel.uiState.value.parallelMillis != null)
        assertEquals(null, viewModel.uiState.value.ratingsLoading)
    }

    @Test
    fun ratingsFailureKeepsCatalogAndAllowsRetry() = runTest(dispatcher) {
        val repository = FakeAppsRepository().apply { ratingFails = true }
        val viewModel = AppListViewModel(repository, LoadRatingsUseCase(repository))
        advanceUntilIdle()
        viewModel.onLoadRatingsClick(RatingsMode.Parallel)
        advanceUntilIdle()
        assertTrue(viewModel.uiState.value.ratingsError)
        assertEquals(repository.apps, viewModel.uiState.value.apps)
        repository.ratingFails = false
        viewModel.onLoadRatingsClick(RatingsMode.Sequential)
        advanceUntilIdle()
        assertFalse(viewModel.uiState.value.ratingsError)
        assertEquals(4.2f, viewModel.uiState.value.ratings.getValue("test"))
    }
}
