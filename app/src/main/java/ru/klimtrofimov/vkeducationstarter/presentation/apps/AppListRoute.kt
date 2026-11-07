package ru.klimtrofimov.vkeducationstarter.presentation.apps

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.collectLatest
import androidx.hilt.navigation.compose.hiltViewModel
import ru.klimtrofimov.vkeducationstarter.R
import ru.klimtrofimov.vkeducationstarter.domain.model.AppInfo

@Composable
fun AppListRoute(
    onAppClick: (AppInfo) -> Unit,
) {
    val viewModel: AppListViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val lifecycleOwner = LocalLifecycleOwner.current
    val toolbarMessage = stringResource(R.string.app_list_toolbar_snackbar)

    LaunchedEffect(viewModel, lifecycleOwner, toolbarMessage) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.effects.collectLatest { effect ->
                when (effect) {
                    AppListEffect.ShowToolbarSnackbar -> snackbarHostState.showSnackbar(toolbarMessage)
                }
            }
        }
    }

    AppListScreen(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onToolbarClick = viewModel::onToolbarClick,
        onAppClick = onAppClick,
        onRetryClick = viewModel::onRetryClick,
    )
}
