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
                navigateToActivity = { lessonId ->
                    navController.navigate("activity/$lessonId")
                }
            )
        }
        composable("activity/{lessonId}") { backStackEntry ->
            val lessonId = backStackEntry.arguments?.getString("lessonId").orEmpty()
            ActivityRoute(lessonId = lessonId) {
                navController.navigate("home") {
                    popUpTo(navController.graph.id) {
                        inclusive = true
                    }
                }
            }
        }
    }
}