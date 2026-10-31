package ru.klimtrofimov.vkeducationstarter.presentation.appdetails

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.klimtrofimov.vkeducationstarter.domain.model.AppDetails
import ru.klimtrofimov.vkeducationstarter.domain.model.AppInfo
import ru.klimtrofimov.vkeducationstarter.domain.repository.AppsRepository

data class AppDetailsUiState(
    val app: AppDetails,
    val showcaseApps: List<AppInfo>,
)

class AppDetailsViewModel(appId: String, repository: AppsRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(
        AppDetailsUiState(
            app = repository.getAppDetails(appId),
            showcaseApps = repository.getApps(),
        )
    )
    val uiState = _uiState.asStateFlow()
}
