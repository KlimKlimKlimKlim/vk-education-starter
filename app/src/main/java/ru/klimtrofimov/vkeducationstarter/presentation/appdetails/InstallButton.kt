package ru.klimtrofimov.vkeducationstarter.presentation.appdetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.klimtrofimov.vkeducationstarter.R

@Composable
fun InstallButton(
    installState: InstallState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Button(
            onClick = onClick,
            enabled = installState != InstallState.Installed,
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(vertical = 12.dp),
            modifier = Modifier.fillMaxWidth().testTag("install_button"),
        ) {
            Text(stringResource(when (installState) {
                InstallState.NotInstalled -> R.string.install
                is InstallState.Installing -> R.string.install_cancel
                InstallState.Installed -> R.string.install_done
            }))
        }
        if (installState is InstallState.Installing) {
            LinearProgressIndicator(
                progress = { installState.progress / 100f },
                modifier = Modifier.fillMaxWidth().testTag("install_progress"),
            )
            Text(stringResource(R.string.install_progress, installState.progress))
        }
    }
}
