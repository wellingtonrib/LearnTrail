package br.com.jwar.triviachallenge.presentation.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import br.com.jwar.triviachallenge.presentation.ui.screens.categories.CategoriesRoute
import br.com.jwar.triviachallenge.presentation.ui.screens.challenge.ChallengeRoute
import br.com.jwar.triviachallenge.presentation.ui.screens.settings.SettingsRoute

@ExperimentalMaterial3Api
@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "categories"
    ) {
        composable("settings") {
            SettingsRoute()
        }
        composable("categories") {
            CategoriesRoute(
                navigateToSettings = {
                    navController.navigate("settings")
                },
                navigateToChallenge = { categoryId, challengeId ->
                    navController.navigate("challenge/$categoryId/$challengeId")
                }
            )
        }
        composable("challenge/{categoryId}/{challengeId}") { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getString("categoryId").orEmpty()
            val challengeId = backStackEntry.arguments?.getString("challengeId").orEmpty()
            ChallengeRoute(categoryId = categoryId, challengeId = challengeId) {
                navController.navigate("categories") {
                    popUpTo(navController.graph.id) {
                        inclusive = true
                    }
                }
            }
        }
    }
}