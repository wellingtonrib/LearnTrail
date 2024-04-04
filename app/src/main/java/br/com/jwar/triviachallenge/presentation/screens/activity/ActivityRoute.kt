package br.com.jwar.triviachallenge.presentation.screens.activity

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.jwar.triviachallenge.R
import br.com.jwar.triviachallenge.presentation.components.ErrorContent
import br.com.jwar.triviachallenge.presentation.components.LoadingContent

@ExperimentalMaterial3Api
@Composable
fun ActivityRoute(
    activityId: String,
    viewModel: ActivityViewModel = hiltViewModel(),
    onNavigateToHome: () -> Unit,
) {
    LaunchedEffect(Unit) {
        viewModel.getActivity(activityId)
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is ActivityViewEffect.NavigateToHome -> onNavigateToHome()
            }
        }
    }

    when(val state = viewModel.uiState.collectAsState().value) {
        is ActivityViewState.Loading ->
            LoadingContent()
        is ActivityViewState.Loaded ->
            ActivityScreen(
                currentQuestion = state.currentQuestion,
                selectedAnswer = state.selectedAnswer,
                attemptsLeft = state.attemptsLeft,
                points = state.points,
                progress = state.progress,
                isResultShown = state.isResultShown,
                isFinished = state.isFinished,
                isSucceeded = state.isSucceeded,
                userMessage = state.userMessages.firstOrNull(),
                onSelectAnswer = viewModel::onSelectAnswer,
                onCheck = viewModel::onCheck,
                onNext = viewModel::onNext,
                onMessageShown = viewModel::onMessageShown
            ) { viewModel.onFinish() }

        is ActivityViewState.Error ->
            ErrorContent(error = stringResource(R.string.error_unknown)) {
                viewModel.getActivity(activityId)
            }
    }
}

