package ru.klimtrofimov.vkeducationstarter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import ru.klimtrofimov.vkeducationstarter.di.AppContainer
import ru.klimtrofimov.vkeducationstarter.presentation.AppNavigation
import ru.klimtrofimov.vkeducationstarter.presentation.theme.VkEducationStarterTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VkEducationStarterTheme {
                AppNavigation(
                    appListViewModelFactory = AppContainer.appListViewModelFactory,
                    appDetailsViewModelFactory = AppContainer::appDetailsViewModelFactory,
                )
            }
        }
    }
}
