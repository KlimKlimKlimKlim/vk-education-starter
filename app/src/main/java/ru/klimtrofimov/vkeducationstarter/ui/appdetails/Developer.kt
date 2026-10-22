package ru.klimtrofimov.vkeducationstarter.ui.appdetails

// Основа: maltsevmicyou/VkEducation, homework-1 (ba94d740).
// Адаптация к локальным данным и ресурсам; компоновка практики сохранена.

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.klimtrofimov.vkeducationstarter.R

@Composable
fun Developer(
    name: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.clickable(onClick = onClick),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column {
            Text(text = name)
            Text(text = stringResource(R.string.app_details_developer))
        }
        IconButton(onClick = onClick) {
            Icon(
                imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
                contentDescription = "О разработчике",
            )
        }
    }
}
