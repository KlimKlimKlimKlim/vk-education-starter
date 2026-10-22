package ru.klimtrofimov.vkeducationstarter.ui.appdetails

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import ru.klimtrofimov.vkeducationstarter.data.AppDetails
import ru.klimtrofimov.vkeducationstarter.ui.apps.Showcase

@Composable
fun AppDetailsScreen(
    app: AppDetails,
    onBackClick: () -> Unit,
    onShowcaseAppClick: () -> Unit,
) {
    val context = LocalContext.current
    var descriptionCollapsed by rememberSaveable(app.id) { mutableStateOf(true) }
    val underDevelopment: () -> Unit = {
        Toast.makeText(context, "В процессе разработки", Toast.LENGTH_SHORT).show()
    }
    Scaffold { padding ->
        AppDetailsContent(
            content = AppDetailsState.Content(app, descriptionCollapsed),
            onBackClick = onBackClick,
            onShareClick = {
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, "${app.name}: ${app.description}")
                }
                context.startActivity(Intent.createChooser(intent, "Поделиться через…"))
            },
            onInstallClick = underDevelopment,
            onReadMoreClick = { descriptionCollapsed = false },
            onDeveloperClick = underDevelopment,
            showcase = { Showcase(onAppClick = onShowcaseAppClick) },
            modifier = Modifier.fillMaxSize().padding(padding),
        )
    }
}
