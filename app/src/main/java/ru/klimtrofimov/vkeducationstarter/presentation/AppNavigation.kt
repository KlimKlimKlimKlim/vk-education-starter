package ru.klimtrofimov.vkeducationstarter.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ru.klimtrofimov.vkeducationstarter.presentation.appdetails.AppDetailsRoute
import ru.klimtrofimov.vkeducationstarter.presentation.apps.AppListRoute

@Composable
fun AppNavigation(
    appListViewModelFactory: ViewModelProvider.Factory,
    appDetailsViewModelFactory: (String) -> ViewModelProvider.Factory,
) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "apps") {
        composable("apps") {
            AppListRoute(viewModelFactory = appListViewModelFactory, onAppClick = { app ->
                navController.navigate("app/${app.id}") { launchSingleTop = true }
            })
        }
        composable(
            route = "app/{appId}",
            arguments = listOf(navArgument("appId") { type = NavType.StringType }),
        ) { entry ->
            val appId = requireNotNull(entry.arguments?.getString("appId"))
            val factory = remember(appId) { appDetailsViewModelFactory(appId) }
            AppDetailsRoute(
                viewModelFactory = factory,
                onBackClick = { navController.popBackStack() },
                onShowcaseAppClick = { navController.popBackStack("apps", inclusive = false) },
            )
        }
    }
}
