package ru.klimtrofimov.vkeducationstarter.ui.apps

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.klimtrofimov.vkeducationstarter.data.AppInfo
import ru.klimtrofimov.vkeducationstarter.data.AppsRepository
import ru.klimtrofimov.vkeducationstarter.data.LocalAppsRepository

data class AppListUiState(val apps: List<AppInfo>)

sealed interface AppListEffect {
    data object ShowToolbarSnackbar : AppListEffect
}

class AppListViewModel(
    repository: AppsRepository = LocalAppsRepository,
) : ViewModel() {
    // Чтение данных один раз на экземпляр ViewModel, не при каждой подписке UI.
    private val _uiState = MutableStateFlow(AppListUiState(repository.getApps()))
    val uiState = _uiState.asStateFlow()

    // Новый экран не получает старое событие после поворота или возврата.
    private val _effects = MutableSharedFlow<AppListEffect>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )
    val effects = _effects.asSharedFlow()

    fun onToolbarClick() {
        _effects.tryEmit(AppListEffect.ShowToolbarSnackbar)
    }
}
