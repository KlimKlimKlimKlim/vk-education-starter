package ru.klimtrofimov.vkeducationstarter

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import androidx.lifecycle.ViewModelStore
import ru.klimtrofimov.vkeducationstarter.presentation.appdetails.AppDetailsState
import ru.klimtrofimov.vkeducationstarter.presentation.appdetails.InstallState
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
        assertEquals("test", (viewModel.state.value as AppDetailsState.Content).appDetails.id)
        assertEquals(repository.apps, (viewModel.state.value as AppDetailsState.Content).showcaseApps)
        assertEquals(0, repository.calls)
        assertEquals(1, repository.detailsCalls)
        assertEquals(0, repository.remoteDetailsCalls)
    }

    @Test
    fun emptyCacheAfterProcessRecreationReloadsOnlyCatalog() = runTest(dispatcher) {
        val repository = FakeAppsRepository()
        val viewModel = AppDetailsViewModel(SavedStateHandle(mapOf("appId" to "test")), repository)
        advanceUntilIdle()
        assertFalse((viewModel.state.value is AppDetailsState.Error))
        assertEquals(1, repository.calls)
        assertEquals(0, repository.remoteDetailsCalls)
    }

    @Test
    fun failedRestoreShowsErrorAndCanRetry() = runTest(dispatcher) {
        val repository = FakeAppsRepository().apply { fail = true }
        val viewModel = AppDetailsViewModel(SavedStateHandle(mapOf("appId" to "test")), repository)
        advanceUntilIdle()
        assertTrue((viewModel.state.value is AppDetailsState.Error))
        repository.fail = false
        viewModel.onRetryClick()
        advanceUntilIdle()
        assertEquals("test", (viewModel.state.value as AppDetailsState.Content).appDetails.id)
        assertEquals(0, repository.remoteDetailsCalls)
    }

    private fun content(viewModel: AppDetailsViewModel) = viewModel.state.value as AppDetailsState.Content

    @Test
    fun installProgressCanBeCancelledAndRestartedFromZero() = runTest(dispatcher) {
        val repository = FakeAppsRepository().apply { cached = apps }
        val viewModel = AppDetailsViewModel(SavedStateHandle(mapOf("appId" to "test")), repository)
        advanceUntilIdle()
        viewModel.onInstallClick()
        assertEquals(InstallState.Installing(0), content(viewModel).installState)
        advanceTimeBy(600)
        runCurrent()
        assertEquals(InstallState.Installing(20), content(viewModel).installState)
        viewModel.onInstallClick()
        assertEquals(InstallState.NotInstalled, content(viewModel).installState)
        advanceTimeBy(5000)
        runCurrent()
        assertEquals(InstallState.NotInstalled, content(viewModel).installState)
        viewModel.onInstallClick()
        assertEquals(InstallState.Installing(0), content(viewModel).installState)
        advanceUntilIdle()
        assertEquals(InstallState.Installed, content(viewModel).installState)
        viewModel.onInstallClick()
        assertEquals(InstallState.Installed, content(viewModel).installState)
    }

    @Test
    fun doubleClickCancelsBeforeFirstDelayAndReadMoreSurvivesProgress() = runTest(dispatcher) {
        val repository = FakeAppsRepository().apply { cached = apps }
        val viewModel = AppDetailsViewModel(SavedStateHandle(mapOf("appId" to "test")), repository)
        advanceUntilIdle()
        viewModel.onInstallClick()
        viewModel.onInstallClick()
        advanceUntilIdle()
        assertEquals(InstallState.NotInstalled, content(viewModel).installState)
        viewModel.onInstallClick()
        viewModel.onReadMoreClick()
        advanceUntilIdle()
        assertFalse(content(viewModel).descriptionCollapsed)
        assertEquals(InstallState.Installed, content(viewModel).installState)
    }

    @Test
    fun clearingViewModelCancelsInstallation() = runTest(dispatcher) {
        val repository = FakeAppsRepository().apply { cached = apps }
        val viewModel = AppDetailsViewModel(SavedStateHandle(mapOf("appId" to "test")), repository)
        val store = ViewModelStore().apply { put("details", viewModel) }
        advanceUntilIdle()
        viewModel.onInstallClick()
        advanceTimeBy(300)
        runCurrent()
        val stateBeforeClear = content(viewModel).installState
        store.clear()
        advanceUntilIdle()
        assertEquals(stateBeforeClear, content(viewModel).installState)
        assertTrue(stateBeforeClear is InstallState.Installing)
    }
}
