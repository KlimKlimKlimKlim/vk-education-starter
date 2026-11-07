package ru.klimtrofimov.vkeducationstarter.presentation.apps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.klimtrofimov.vkeducationstarter.domain.model.AppInfo
import ru.klimtrofimov.vkeducationstarter.domain.repository.AppsRepository

data class AppListUiState(
    val apps: List<AppInfo> = emptyList(),
    val isLoading: Boolean = true,
    val hasError: Boolean = false,
)

sealed interface AppListEffect {
    data object ShowToolbarSnackbar : AppListEffect
}

@HiltViewModel
class AppListViewModel @Inject constructor(
    private val repository: AppsRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(AppListUiState())
    val uiState = _uiState.asStateFlow()

    // Новый экран не получает старое событие после поворота или возврата.
    private val _effects = MutableSharedFlow<AppListEffect>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )
    val effects = _effects.asSharedFlow()

    init {
        loadApps()
    }

    fun onRetryClick() {
        if (!_uiState.value.isLoading) loadApps()
    }

    private fun loadApps() {
        _uiState.value = AppListUiState()
        viewModelScope.launch {
            try {
                _uiState.value = AppListUiState(apps = repository.getApps(), isLoading = false)
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (_: Exception) {
                _uiState.value = AppListUiState(isLoading = false, hasError = true)
            }
        }
    }

    fun onToolbarClick() {
        _effects.tryEmit(AppListEffect.ShowToolbarSnackbar)
    }
}
