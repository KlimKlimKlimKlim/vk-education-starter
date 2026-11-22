package ru.klimtrofimov.vkeducationstarter.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ru.klimtrofimov.vkeducationstarter.presentation.appdetails.AppDetailsRoute
import ru.klimtrofimov.vkeducationstarter.presentation.apps.AppListRoute

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "apps") {
        composable("apps") {
            AppListRoute(onAppClick = { app ->
                navController.navigate("app/${app.id}") { launchSingleTop = true }
            })
        }
        composable(
            route = "app/{appId}",
            arguments = listOf(navArgument("appId") { type = NavType.StringType }),
        ) {
            AppDetailsRoute(
                onBackClick = { navController.popBackStack() },
                onShowcaseAppClick = { navController.popBackStack("apps", inclusive = false) },
            )
        }
    }
}
