package ru.klimtrofimov.vkeducationstarter.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ru.klimtrofimov.vkeducationstarter.data.SampleApps
import ru.klimtrofimov.vkeducationstarter.ui.appdetails.AppDetailsScreen
import ru.klimtrofimov.vkeducationstarter.ui.apps.AppListScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "apps") {
        composable("apps") {
            AppListScreen(onAppClick = { app ->
                navController.navigate("app/${app.id}") { launchSingleTop = true }
            })
        }
        composable(
            route = "app/{appId}",
            arguments = listOf(navArgument("appId") { type = NavType.StringType }),
        ) { entry ->
            val appId = requireNotNull(entry.arguments?.getString("appId"))
            AppDetailsScreen(
                app = SampleApps.details(appId),
                onBackClick = { navController.popBackStack() },
                onShowcaseAppClick = { navController.popBackStack("apps", inclusive = false) },
            )
        }
    }
}
