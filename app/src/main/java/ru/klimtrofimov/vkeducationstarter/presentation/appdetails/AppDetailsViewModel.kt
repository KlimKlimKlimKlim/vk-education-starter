package ru.klimtrofimov.vkeducationstarter.presentation.appdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.klimtrofimov.vkeducationstarter.domain.repository.AppsRepository

@HiltViewModel
class AppDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: AppsRepository,
) : ViewModel() {
    private val appId: String = checkNotNull(savedStateHandle["appId"])
    private val _state = MutableStateFlow<AppDetailsState>(AppDetailsState.Loading)
    val state = _state.asStateFlow()
    private var installJob: Job? = null

    init {
        loadApp()
    }

    fun onRetryClick() {
        if (_state.value is AppDetailsState.Error) loadApp()
    }

    private fun loadApp() {
        _state.value = AppDetailsState.Loading
        viewModelScope.launch {
            try {
                val apps = repository.getCachedApps().ifEmpty { repository.getApps() }
                _state.value = AppDetailsState.Content(
                    appDetails = repository.getAppDetails(appId),
                    showcaseApps = apps.take(9),
                )
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (_: Exception) {
                _state.value = AppDetailsState.Error
            }
        }
    }

    fun onReadMoreClick() {
        _state.update { current ->
            if (current is AppDetailsState.Content) current.copy(descriptionCollapsed = false)
            else current
        }
    }

    fun onInstallClick() {
        val current = _state.value as? AppDetailsState.Content ?: return
        when (current.installState) {
            is InstallState.Installing -> {
                installJob?.cancel()
                installJob = null
                updateInstallState(InstallState.NotInstalled)
            }
            InstallState.NotInstalled -> {
                // До launch: повторный быстрый клик уже видит Installing и отменяет Job.
                updateInstallState(InstallState.Installing(0))
                installJob = viewModelScope.launch {
                    emulateInstall()
                    installJob = null
                }
            }
            InstallState.Installed -> Unit
        }
    }

    private suspend fun emulateInstall() {
        for (progress in 5..100 step 5) {
            delay(150)
            updateInstallState(InstallState.Installing(progress))
        }
        updateInstallState(InstallState.Installed)
    }

    private fun updateInstallState(installState: InstallState) {
        _state.update { current ->
            if (current is AppDetailsState.Content) current.copy(installState = installState)
            else current
        }
    }
}
