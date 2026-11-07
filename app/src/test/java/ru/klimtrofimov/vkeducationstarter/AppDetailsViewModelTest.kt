package ru.klimtrofimov.vkeducationstarter

import androidx.lifecycle.SavedStateHandle
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
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import ru.klimtrofimov.vkeducationstarter.presentation.appdetails.AppDetailsViewModel

@OptIn(ExperimentalCoroutinesApi::class)
class AppDetailsViewModelTest {
    private val dispatcher = StandardTestDispatcher()
    @Before fun setUp() = Dispatchers.setMain(dispatcher)
    @After fun tearDown() = Dispatchers.resetMain()

    @Test
    fun cardAndShowcaseUseCacheWithoutDetailedOrCatalogRequests() = runTest(dispatcher) {
        val repository = FakeAppsRepository().apply { cached = apps }
        val viewModel = AppDetailsViewModel(SavedStateHandle(mapOf("appId" to "test")), repository)
        advanceUntilIdle()
        assertEquals("test", viewModel.uiState.value.app!!.id)
        assertEquals(repository.apps, viewModel.uiState.value.showcaseApps)
        assertEquals(0, repository.calls)
        assertEquals(1, repository.detailsCalls)
        assertEquals(0, repository.remoteDetailsCalls)
    }

    @Test
    fun emptyCacheAfterProcessRecreationReloadsOnlyCatalog() = runTest(dispatcher) {
        val repository = FakeAppsRepository()
        val viewModel = AppDetailsViewModel(SavedStateHandle(mapOf("appId" to "test")), repository)
        advanceUntilIdle()
        assertFalse(viewModel.uiState.value.hasError)
        assertEquals(1, repository.calls)
        assertEquals(0, repository.remoteDetailsCalls)
    }

    @Test
    fun failedRestoreShowsErrorAndCanRetry() = runTest(dispatcher) {
        val repository = FakeAppsRepository().apply { fail = true }
        val viewModel = AppDetailsViewModel(SavedStateHandle(mapOf("appId" to "test")), repository)
        advanceUntilIdle()
        assertTrue(viewModel.uiState.value.hasError)
        repository.fail = false
        viewModel.onRetryClick()
        advanceUntilIdle()
        assertEquals("test", viewModel.uiState.value.app!!.id)
        assertEquals(0, repository.remoteDetailsCalls)
    }
}
