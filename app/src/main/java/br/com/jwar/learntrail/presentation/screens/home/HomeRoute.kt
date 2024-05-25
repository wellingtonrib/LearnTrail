package br.com.jwar.learntrail.presentation.screens.home

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.jwar.learntrail.R
import br.com.jwar.learntrail.presentation.components.ErrorContent
import br.com.jwar.learntrail.presentation.components.LoadingContent

@ExperimentalMaterial3Api
@Composable
fun HomeRoute(
    viewModel: HomeViewModel = hiltViewModel(),
    navigateToSettings: () -> Unit,
    navigateToActivity: (String) -> Unit,
) {
    LaunchedEffect(Unit) {
        viewModel.onIntent(HomeViewIntent.LoadUnits)
        viewModel.uiEffect.collect { effect ->
            when(effect) {
                is HomeViewEffect.NavigateToActivity -> navigateToActivity(effect.activityId)
                is HomeViewEffect.NavigateToSettings -> navigateToSettings()
            }
        }
    }

    when(val state = viewModel.uiState.collectAsStateWithLifecycle().value) {
        is HomeViewState.Loading ->
            LoadingContent()
        is HomeViewState.Loaded ->
            HomeContent(
                userXP = state.userXP,
                units = state.units,
                isRefreshing = state.isRefreshing,
                onIntent = viewModel::onIntent,
            )
        is HomeViewState.Error ->
            ErrorContent(error = stringResource(R.string.error_unknown)) {
                viewModel.onIntent(HomeViewIntent.LoadUnits)
            }
    }
}