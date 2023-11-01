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
    categoryId: String?,
    viewModel: ChallengeViewModel = hiltViewModel(),
    onNavigateToCategories: () -> Unit,
) {
    LaunchedEffect(Unit) {
        viewModel.getChallenge(categoryId.orEmpty())
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
                nextQuestion = state.nextQuestion,
                selectedAnswer = state.selectedAnswer,
                isResultShown = state.isResultShown,
                isLastQuestion = state.isLastQuestion,
                onSelectAnswer = { viewModel.onSelectAnswer(it) },
                onCheck = { viewModel.onCheck() },
                onNext = { viewModel.onNext() },
                onFinish = { viewModel.onFinish() }
            )
        is ChallengeViewState.Error ->
            ErrorContent(error = state.error.localizedMessage ?: stringResource(R.string.error_unknown)) {
                viewModel.getChallenge(categoryId.orEmpty())
            }
    }
}

