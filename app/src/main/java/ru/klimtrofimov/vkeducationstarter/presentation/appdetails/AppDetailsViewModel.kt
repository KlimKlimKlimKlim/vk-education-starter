package ru.klimtrofimov.vkeducationstarter.presentation.appdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.klimtrofimov.vkeducationstarter.domain.model.AppDetails
import ru.klimtrofimov.vkeducationstarter.domain.model.AppInfo
import ru.klimtrofimov.vkeducationstarter.domain.repository.AppsRepository

data class AppDetailsUiState(
    val app: AppDetails,
    val showcaseApps: List<AppInfo>,
)

@HiltViewModel
class AppDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    repository: AppsRepository,
) : ViewModel() {
    private val appId: String = checkNotNull(savedStateHandle["appId"]) {
        "App details route requires appId"
    }

    private val _uiState = MutableStateFlow(
        AppDetailsUiState(
            app = repository.getAppDetails(appId),
            showcaseApps = repository.getApps(),
        )
    )
    val uiState = _uiState.asStateFlow()
}
