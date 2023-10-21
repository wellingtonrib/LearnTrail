package br.com.jwar.triviachallenge.presentation.ui.screens.challenge

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.jwar.triviachallenge.domain.repositories.ChallengeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChallengeViewModel @Inject constructor(
    private val challengeRepository: ChallengeRepository
): ViewModel() {

    private val _uiState = MutableStateFlow<ChallengeViewState>(ChallengeViewState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _uiEffect = Channel<ChallengeViewEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()

    fun getChallenge(categoryId: String) = viewModelScope.launch {
        challengeRepository.getChallenge(categoryId)
            .onStart { _uiState.update { ChallengeViewState.Loading } }
            .catch { error -> _uiState.update { ChallengeViewState.Error(error) } }
            .collect { challenge -> _uiState.update { ChallengeViewState.Loaded(challenge) } }
    }

    fun onSelectAnswer(answer: String) {
        _uiState.updateLoadedState { it.copy(selectedAnswer = answer) }
    }

    fun onCheck() {
        _uiState.updateLoadedState { it.copy(isResultShown = true) }
    }

    fun onNext() {
        _uiState.updateLoadedState { state ->
            val currentQuestionIndex = state.challenge.questions.indexOf(state.nextQuestion)
            if (currentQuestionIndex < state.challenge.questions.size - 1) {
                val nextQuestion = state.challenge.questions[currentQuestionIndex + 1]
                state.copy(
                    nextQuestion = nextQuestion,
                    selectedAnswer = null,
                    isResultShown = false,
                    isLastQuestion = state.challenge.questions.last() == nextQuestion
                )
            } else {
                state.also { onFinish() }
            }
        }
    }

    fun onFinish() = viewModelScope.launch {
        _uiEffect.send(ChallengeViewEffect.NavigateToCategories)
    }

}

private fun MutableStateFlow<ChallengeViewState>.updateLoadedState(
    block: (ChallengeViewState.Loaded) -> ChallengeViewState.Loaded
) = this.update { state -> (state as? ChallengeViewState.Loaded)?.let { block(it) } ?: state }