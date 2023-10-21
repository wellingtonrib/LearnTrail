package br.com.jwar.triviachallenge.presentation.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import br.com.jwar.triviachallenge.presentation.ui.screens.categories.CategoriesRoute
import br.com.jwar.triviachallenge.presentation.ui.screens.challenge.ChallengeRoute

@ExperimentalMaterial3Api
@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "categories"
    ) {
        composable("categories") {
            CategoriesRoute { categoryId ->
                navController.navigate("challenge/$categoryId")
            }
        }
        composable("challenge/{categoryId}") { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getString("categoryId").orEmpty()
            ChallengeRoute(categoryId = categoryId) {
                navController.navigate("categories") {
                    popUpTo(navController.graph.id) {
                        inclusive = true
                    }
                }
            }
        }
    }
}