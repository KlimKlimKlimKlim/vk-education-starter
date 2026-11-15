package ru.klimtrofimov.vkeducationstarter.presentation.appdetails

import ru.klimtrofimov.vkeducationstarter.domain.model.AppDetails
import ru.klimtrofimov.vkeducationstarter.domain.model.AppInfo

sealed interface InstallState {
    data object NotInstalled : InstallState
    data class Installing(val progress: Int) : InstallState
    data object Installed : InstallState
}

sealed interface AppDetailsState {
    data object Loading : AppDetailsState
    data object Error : AppDetailsState
    data class Content(
        val appDetails: AppDetails,
        val descriptionCollapsed: Boolean = true,
        val showcaseApps: List<AppInfo> = emptyList(),
        val installState: InstallState = InstallState.NotInstalled,
    ) : AppDetailsState
}
