package ru.klimtrofimov.vkeducationstarter.presentation.apps

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import ru.klimtrofimov.vkeducationstarter.R
import ru.klimtrofimov.vkeducationstarter.domain.model.AppInfo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppListScreen(
    uiState: AppListUiState,
    snackbarHostState: SnackbarHostState,
    onToolbarClick: () -> Unit,
    onAppClick: (AppInfo) -> Unit,
    onRetryClick: () -> Unit,
) {
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .testTag("app_list_toolbar")
                    .clickable(
                        role = Role.Button,
                        onClickLabel = stringResource(R.string.app_list_toolbar_click),
                        onClick = onToolbarClick,
                    ),
                title = { Text(stringResource(R.string.app_list_title)) },
                navigationIcon = {
                    Image(
                        painter = painterResource(R.drawable.ic_launcher_foreground),
                        contentDescription = stringResource(R.string.app_name),
                        modifier = Modifier.padding(start = 8.dp).size(48.dp),
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
            )
        },
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
            when {
                uiState.isLoading -> CircularProgressIndicator(Modifier.testTag("catalog_loading"))
                uiState.hasError -> Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.padding(16.dp),
                ) {
                    Text(stringResource(R.string.catalog_error))
                    Button(onClick = onRetryClick) {
                        Text(stringResource(R.string.catalog_retry))
                    }
                }
                uiState.apps.isEmpty() -> Text(stringResource(R.string.catalog_empty))
                else -> LazyColumn(modifier = Modifier.fillMaxSize().testTag("app_list")) {
                    items(items = uiState.apps, key = { it.id }) { app ->
                        AppListItem(app = app, onClick = { onAppClick(app) })
                    }
                }
            }
        }
    }
}
