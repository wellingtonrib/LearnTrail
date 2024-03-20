package br.com.jwar.triviachallenge.presentation.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import br.com.jwar.triviachallenge.presentation.ui.screens.home.HomeRoute
import br.com.jwar.triviachallenge.presentation.ui.screens.activity.ActivityRoute
import br.com.jwar.triviachallenge.presentation.ui.screens.settings.SettingsRoute

@ExperimentalMaterial3Api
@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("settings") {
            SettingsRoute()
        }
        composable("home") {
            HomeRoute(
                navigateToSettings = {
                    navController.navigate("settings")
                },
                navigateToHome = { unitId, activityId ->
                    navController.navigate("activity/$unitId/$activityId")
                }
            )
        }
        composable("activity/{unitId}/{activityId}") { backStackEntry ->
            val unitId = backStackEntry.arguments?.getString("unitId").orEmpty()
            val activityId = backStackEntry.arguments?.getString("activityId").orEmpty()
            ActivityRoute(unitId = unitId, activityId = activityId) {
                navController.navigate("home") {
                    popUpTo(navController.graph.id) {
                        inclusive = true
                    }
                }
            }
        }
    }
}