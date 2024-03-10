package br.com.jwar.triviachallenge.presentation.ui.screens.categories

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.jwar.triviachallenge.R
import br.com.jwar.triviachallenge.presentation.ui.components.ErrorContent
import br.com.jwar.triviachallenge.presentation.ui.components.LoadingContent

@ExperimentalMaterial3Api
@Composable
fun CategoriesRoute(
    viewModel: CategoriesViewModel = hiltViewModel(),
    navigateToSettings: () -> Unit,
    navigateToChallenge: (String, String) -> Unit,
) {
    LaunchedEffect(Unit) {
        viewModel.getCategories()
        viewModel.uiEffect.collect { effect ->
            when(effect) {
                is CategoriesViewEffect.NavigateToChallenge -> navigateToChallenge(effect.categoryId, effect.challengeId)
                is CategoriesViewEffect.NavigateToSettings -> navigateToSettings()
            }
        }
    }

    when(val state = viewModel.uiState.collectAsState().value) {
        is CategoriesViewState.Loading ->
            LoadingContent()
        is CategoriesViewState.Loaded ->
            CategoriesScreen(
                categories = state.categories,
                onNavigateToSettings = viewModel::onNavigateToSettings,
                onNavigateToChallenge = viewModel::onNavigateToChallenge
            )
        is CategoriesViewState.Error ->
            ErrorContent(error = state.error.localizedMessage ?: stringResource(R.string.error_unknown)) {
                viewModel.getCategories()
            }
    }
}