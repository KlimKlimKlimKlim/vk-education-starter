package ru.klimtrofimov.vkeducationstarter.ui.apps

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.collectLatest
import ru.klimtrofimov.vkeducationstarter.R
import ru.klimtrofimov.vkeducationstarter.data.AppInfo

@Composable
fun AppListRoute(
    onAppClick: (AppInfo) -> Unit,
    viewModel: AppListViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    // Snackbar не сохраняется через rememberSaveable: после поворота он не повторяется.
    val snackbarHostState = remember { SnackbarHostState() }
    val lifecycleOwner = LocalLifecycleOwner.current
    val snackbarText = stringResource(R.string.app_list_toolbar_snackbar)

    LaunchedEffect(viewModel, lifecycleOwner, snackbarText) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.effects.collectLatest { effect ->
                when (effect) {
                    AppListEffect.ShowToolbarSnackbar -> snackbarHostState.showSnackbar(snackbarText)
                }
            }
        }
    }

    AppListScreen(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onToolbarClick = viewModel::onToolbarClick,
        onAppClick = onAppClick,
    )
}
