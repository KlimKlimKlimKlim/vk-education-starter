package ru.klimtrofimov.vkeducationstarter.ui.apps

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.klimtrofimov.vkeducationstarter.R
import ru.klimtrofimov.vkeducationstarter.data.AppInfo
import ru.klimtrofimov.vkeducationstarter.data.SampleApps

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppListScreen(onAppClick: ((AppInfo) -> Unit)? = null) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Приложения") },
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
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
        ) {
            items(items = SampleApps.apps, key = { it.id }) { app ->
                AppListItem(
                    app = app,
                    onClick = onAppClick?.let { callback -> { callback(app) } },
                )
            }
        }
    }
}
