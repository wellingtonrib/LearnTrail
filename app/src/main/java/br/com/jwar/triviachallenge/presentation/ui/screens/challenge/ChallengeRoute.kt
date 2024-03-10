package br.com.jwar.triviachallenge.presentation.ui.screens.challenge

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
fun ChallengeRoute(
    categoryId: String,
    challengeId: String,
    viewModel: ChallengeViewModel = hiltViewModel(),
    onNavigateToCategories: () -> Unit,
) {
    LaunchedEffect(Unit) {
        viewModel.getChallenge(categoryId, challengeId)
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is ChallengeViewEffect.NavigateToCategories -> onNavigateToCategories()
            }
        }
    }

    when(val state = viewModel.uiState.collectAsState().value) {
        is ChallengeViewState.Loading ->
            LoadingContent()
        is ChallengeViewState.Loaded ->
            ChallengeScreen(
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

        is ChallengeViewState.Error ->
            ErrorContent(error = state.error.localizedMessage ?: stringResource(R.string.error_unknown)) {
                viewModel.getChallenge(categoryId, challengeId)
            }
    }
}

