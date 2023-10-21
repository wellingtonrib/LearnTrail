package br.com.jwar.triviachallenge.presentation.ui.screens.categories

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.jwar.triviachallenge.presentation.ui.components.ErrorContent
import br.com.jwar.triviachallenge.presentation.ui.components.LoadingContent

@ExperimentalMaterial3Api
@Composable
fun CategoriesRoute(
    viewModel: CategoriesViewModel = hiltViewModel(),
    navigateToChallenge: (String) -> Unit,
) {
    LaunchedEffect(Unit) {
        viewModel.getCategories()
        viewModel.uiEffect.collect { effect ->
            when(effect) {
                is CategoriesViewEffect.NavigateToChallenge -> navigateToChallenge(effect.categoryId)
            }
        }
    }

    when(val state = viewModel.uiState.collectAsState().value) {
        is CategoriesViewState.Loading ->
            LoadingContent()
        is CategoriesViewState.Loaded ->
            CategoriesScreen(state.categories) { categoryId ->
                viewModel.onSelectCategory(categoryId)
            }
        is CategoriesViewState.Error ->
            ErrorContent(error = state.error.localizedMessage ?: "Unknown error") {
                viewModel.getCategories()
            }
    }
}