package ru.klimtrofimov.vkeducationstarter.presentation.appdetails

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun AppDetailsRoute(
    viewModelFactory: ViewModelProvider.Factory,
    onBackClick: () -> Unit,
    onShowcaseAppClick: () -> Unit,
    viewModel: AppDetailsViewModel = viewModel(factory = viewModelFactory),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    AppDetailsScreen(
        app = uiState.app,
        showcaseApps = uiState.showcaseApps,
        onBackClick = onBackClick,
        onShowcaseAppClick = onShowcaseAppClick,
    )
}
