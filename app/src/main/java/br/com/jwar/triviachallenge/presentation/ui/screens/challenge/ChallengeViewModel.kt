package br.com.jwar.triviachallenge.presentation.ui.screens.challenge

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.jwar.triviachallenge.R
import br.com.jwar.triviachallenge.domain.repositories.ChallengeRepository
import br.com.jwar.triviachallenge.presentation.model.UIMessage
import br.com.jwar.triviachallenge.presentation.model.UIMessageStyle
import br.com.jwar.triviachallenge.presentation.model.UIText
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
        _uiState.updateLoadedState { state ->
            state.copy(selectedAnswer = answer)
        }
    }

    fun onCheck() {
        _uiState.updateLoadedState { state ->
            val isCorrectAnswer = state.selectedAnswer == state.currentQuestion.correctAnswer
            val attemptsLeft = if (isCorrectAnswer) state.attemptsLeft else state.attemptsLeft - 1
            val points = if (isCorrectAnswer) state.points + 1 else state.points
            val messageRes = if (isCorrectAnswer) R.string.message_correct_answer else R.string.message_wrong_answer
            val messageStyle = if (isCorrectAnswer) UIMessageStyle.SUCCESS else UIMessageStyle.DANGER
            state.copy(
                isResultShown = true,
                attemptsLeft = attemptsLeft,
                points = points,
                userMessages = state.userMessages + UIMessage(
                    text = UIText.StringResource(messageRes),
                    style = messageStyle
                )
            )
        }
    }

    fun onNext() {
        _uiState.updateLoadedState { state ->
            val hasAttemptsRemaining = state.attemptsLeft > 0
            val currentQuestionIndex = state.challenge.questions.indexOf(state.currentQuestion)
            val hasNextQuestion = currentQuestionIndex < state.challenge.questions.size - 1
            if (hasAttemptsRemaining && hasNextQuestion) {
                val nextQuestionIndex = currentQuestionIndex + 1
                val nextQuestion = state.challenge.questions[nextQuestionIndex]
                val progress = "${nextQuestionIndex + 1}/${state.challenge.questions.size}"
                state.copy(
                    currentQuestion = nextQuestion,
                    selectedAnswer = null,
                    isResultShown = false,
                    progress = progress,
                )
            } else {
                state.copy(
                    isFinished = true,
                    isSucceeded = hasAttemptsRemaining,
                )
            }
        }
    }

    fun onFinish() = viewModelScope.launch {
        _uiEffect.send(ChallengeViewEffect.NavigateToCategories)
    }

    fun onMessageShown(uiMessage: UIMessage) {
        _uiState.updateLoadedState { state ->
            state.copy(userMessages = state.userMessages - uiMessage)
        }
    }
}

private fun MutableStateFlow<ChallengeViewState>.updateLoadedState(
    block: (ChallengeViewState.Loaded) -> ChallengeViewState.Loaded
) = this.update { state -> (state as? ChallengeViewState.Loaded)?.let { block(it) } ?: state }