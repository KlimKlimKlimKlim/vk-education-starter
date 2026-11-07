package ru.klimtrofimov.vkeducationstarter.presentation.appdetails

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun AppDetailsRoute(
    onBackClick: () -> Unit,
    onShowcaseAppClick: () -> Unit,
    viewModel: AppDetailsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    AppDetailsScreen(
        app = uiState.app,
        showcaseApps = uiState.showcaseApps,
        onBackClick = onBackClick,
        onShowcaseAppClick = onShowcaseAppClick,
    )
}
