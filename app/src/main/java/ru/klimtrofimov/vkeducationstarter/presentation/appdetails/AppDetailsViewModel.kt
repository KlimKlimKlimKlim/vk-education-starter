package ru.klimtrofimov.vkeducationstarter.presentation.appdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.klimtrofimov.vkeducationstarter.domain.model.AppDetails
import ru.klimtrofimov.vkeducationstarter.domain.model.AppInfo
import ru.klimtrofimov.vkeducationstarter.domain.repository.AppsRepository

data class AppDetailsUiState(
    val app: AppDetails? = null,
    val showcaseApps: List<AppInfo> = emptyList(),
    val isLoading: Boolean = true,
    val hasError: Boolean = false,
)

@HiltViewModel
class AppDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: AppsRepository,
) : ViewModel() {
    private val appId: String = checkNotNull(savedStateHandle["appId"])
    private val _uiState = MutableStateFlow(AppDetailsUiState())
    val uiState: StateFlow<AppDetailsUiState> = _uiState.asStateFlow()

    init {
        loadApp()
    }

    fun onRetryClick() {
        if (!_uiState.value.isLoading) loadApp()
    }

    private fun loadApp() {
        _uiState.value = AppDetailsUiState()
        viewModelScope.launch {
            try {
                // После process death восстанавливаем только каталог, не подробный endpoint.
                val apps = repository.getCachedApps().ifEmpty { repository.getApps() }
                _uiState.value = AppDetailsUiState(
                    app = repository.getAppDetails(appId),
                    showcaseApps = apps.take(9),
                    isLoading = false,
                )
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (_: Exception) {
                _uiState.value = AppDetailsUiState(isLoading = false, hasError = true)
            }
        }
    }
}
