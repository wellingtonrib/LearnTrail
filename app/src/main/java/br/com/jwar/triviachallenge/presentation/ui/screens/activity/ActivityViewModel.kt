package br.com.jwar.triviachallenge.presentation.ui.screens.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.jwar.triviachallenge.R
import br.com.jwar.triviachallenge.data.datasources.local.Progression
import br.com.jwar.triviachallenge.domain.model.Activity
import br.com.jwar.triviachallenge.domain.repositories.ActivityRepository
import br.com.jwar.triviachallenge.presentation.utils.UIMessage
import br.com.jwar.triviachallenge.presentation.utils.UIMessageStyle
import br.com.jwar.triviachallenge.presentation.utils.UIText
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
class ActivityViewModel @Inject constructor(
    private val activityRepository: ActivityRepository
): ViewModel() {

    private val _uiState = MutableStateFlow<ActivityViewState>(ActivityViewState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _uiEffect = Channel<ActivityViewEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()

    fun getActivity(lessonId: String) = viewModelScope.launch {
        activityRepository.getActivity(lessonId)
            .onStart { setLoadingState() }
            .catch { error -> setErrorState(error) }
            .collect { activity -> setLoadedState(activity) }
    }

    private fun setLoadedState(activity: Activity) {
        _uiState.update {
            ActivityViewState.Loaded(activity, activity.questions.first())
        }
    }

    private fun setErrorState(error: Throwable) {
        _uiState.update { ActivityViewState.Error(error) }
    }

    private fun setLoadingState() {
        _uiState.update { ActivityViewState.Loading }
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
            val currentQuestionIndex = state.activity.questions.indexOf(state.currentQuestion)
            val hasNextQuestion = currentQuestionIndex < state.activity.questions.size - 1
            if (hasAttemptsRemaining && hasNextQuestion) {
                val nextQuestionIndex = currentQuestionIndex + 1
                val nextQuestion = state.activity.questions[nextQuestionIndex]
                val progress = "${nextQuestionIndex + 1}/${state.activity.questions.size}"
                state.copy(
                    currentQuestion = nextQuestion,
                    selectedAnswer = null,
                    isResultShown = false,
                    progress = progress,
                )
            } else {
                if (hasAttemptsRemaining) {
                    Progression.lessonsCompleted.add(state.activity.lessonId)
                }
                state.copy(
                    isFinished = true,
                    isSucceeded = hasAttemptsRemaining,
                )
            }
        }
    }

    fun onFinish() = viewModelScope.launch {
        _uiEffect.send(ActivityViewEffect.NavigateToHome)
    }

    fun onMessageShown(uiMessage: UIMessage) {
        _uiState.updateLoadedState { state ->
            state.copy(userMessages = state.userMessages - uiMessage)
        }
    }
}

private fun MutableStateFlow<ActivityViewState>.updateLoadedState(
    block: (ActivityViewState.Loaded) -> ActivityViewState.Loaded
) = this.update { state -> (state as? ActivityViewState.Loaded)?.let { block(it) } ?: state }