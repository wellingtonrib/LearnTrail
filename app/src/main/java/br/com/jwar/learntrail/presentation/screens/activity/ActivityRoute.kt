package br.com.jwar.learntrail.presentation.screens.activity

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
fun ActivityRoute(
    activityId: String,
    viewModel: ActivityViewModel = hiltViewModel(),
    onNavigateToHome: () -> Unit,
) {
    LaunchedEffect(Unit) {
        viewModel.onIntent(ActivityViewIntent.LoadActivity(activityId))
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is ActivityViewEffect.NavigateToHome -> onNavigateToHome()
            }
        }
    }

    when(val state = viewModel.uiState.collectAsStateWithLifecycle().value) {
        is ActivityViewState.Loading ->
            LoadingContent()
        is ActivityViewState.Loaded ->
            ActivityContent(state = state, onIntent = viewModel::onIntent)
        is ActivityViewState.Error ->
            ErrorContent(error = stringResource(R.string.error_unknown)) {
                viewModel.onIntent(ActivityViewIntent.LoadActivity(activityId))
            }
    }
}

