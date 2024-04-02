package br.com.jwar.triviachallenge.presentation.ui.screens.home

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
fun HomeRoute(
    viewModel: HomeViewModel = hiltViewModel(),
    navigateToSettings: () -> Unit,
    navigateToActivity: (String) -> Unit,
) {
    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when(effect) {
                is HomeViewEffect.NavigateToActivity -> navigateToActivity(effect.activityId)
                is HomeViewEffect.NavigateToSettings -> navigateToSettings()
            }
        }
    }

    when(val state = viewModel.uiState.collectAsState().value) {
        is HomeViewState.Loading ->
            LoadingContent()
        is HomeViewState.Loaded ->
            HomeScreen(
                units = state.units,
                isRefreshing = state.isRefreshing,
                onNavigateToSettings = viewModel::onNavigateToSettings,
                onNavigateToActivity = viewModel::onNavigateToActivity,
                onRefresh = viewModel::onRefresh,
            )
        is HomeViewState.Error ->
            ErrorContent(error = stringResource(R.string.error_unknown)) {
                viewModel.getUnits()
            }
    }
}