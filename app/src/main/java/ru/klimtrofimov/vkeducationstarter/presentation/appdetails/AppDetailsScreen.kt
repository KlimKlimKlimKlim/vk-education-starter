package ru.klimtrofimov.vkeducationstarter.presentation.appdetails

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import ru.klimtrofimov.vkeducationstarter.presentation.apps.Showcase

@Composable
fun AppDetailsScreen(
    content: AppDetailsState.Content,
    onInstallClick: () -> Unit,
    onReadMoreClick: () -> Unit,
    onBackClick: () -> Unit,
    onShowcaseAppClick: () -> Unit,
) {
    val context = LocalContext.current
    val app = content.appDetails
    val underDevelopment: () -> Unit = {
        Toast.makeText(context, "В процессе разработки", Toast.LENGTH_SHORT).show()
    }
    Scaffold { padding ->
        AppDetailsContent(
            content = content,
            onBackClick = onBackClick,
            onShareClick = {
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, "${app.name}: ${app.description}")
                }
                context.startActivity(Intent.createChooser(intent, "Поделиться через…"))
            },
            onInstallClick = onInstallClick,
            onReadMoreClick = onReadMoreClick,
            onDeveloperClick = underDevelopment,
            showcase = { Showcase(apps = content.showcaseApps, onAppClick = onShowcaseAppClick) },
            modifier = Modifier.fillMaxSize().padding(padding),
        )
    }
}
