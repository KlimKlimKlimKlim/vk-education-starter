package ru.klimtrofimov.vkeducationstarter.presentation.appdetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.klimtrofimov.vkeducationstarter.R

@Composable
fun AppDetailsRoute(
    onBackClick: () -> Unit,
    onShowcaseAppClick: () -> Unit,
) {
    val viewModel: AppDetailsViewModel = hiltViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val content = state as? AppDetailsState.Content
    if (content != null) {
        AppDetailsScreen(
            content = content,
            onInstallClick = viewModel::onInstallClick,
            onReadMoreClick = viewModel::onReadMoreClick,
            onBackClick = onBackClick,
            onShowcaseAppClick = onShowcaseAppClick,
        )
    } else {
        Scaffold { padding ->
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                if (state is AppDetailsState.Loading) {
                    CircularProgressIndicator()
                } else {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.padding(16.dp),
                    ) {
                        Text(stringResource(R.string.app_details_error))
                        Button(onClick = viewModel::onRetryClick) {
                            Text(stringResource(R.string.catalog_retry))
                        }
                        Button(onClick = onBackClick) { Text("Назад") }
                    }
                }
            }
        }
    }
}
