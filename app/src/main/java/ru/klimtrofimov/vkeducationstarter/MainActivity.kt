package ru.klimtrofimov.vkeducationstarter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import ru.klimtrofimov.vkeducationstarter.presentation.AppNavigation
import ru.klimtrofimov.vkeducationstarter.presentation.theme.VkEducationStarterTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VkEducationStarterTheme {
                AppNavigation()
            }
        }
    }
}
