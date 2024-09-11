package br.com.jwar.learntrail.presentation.screens.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.jwar.learntrail.R
import br.com.jwar.learntrail.domain.model.Question
import br.com.jwar.learntrail.domain.repositories.ActivityRepository
import br.com.jwar.learntrail.domain.repositories.UserRepository
import br.com.jwar.learntrail.presentation.utils.UIMessage
import br.com.jwar.learntrail.presentation.utils.UIMessageStyle
import br.com.jwar.learntrail.presentation.utils.UIText
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
    private val activityRepository: ActivityRepository,
    private val userRepository: UserRepository,
): ViewModel() {

    private val _uiState = MutableStateFlow<ActivityViewState>(ActivityViewState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _uiEffect = Channel<ActivityViewEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()

    fun onIntent(intent: ActivityViewIntent) {
        when (intent) {
            is ActivityViewIntent.LoadActivity -> onLoadActivity(intent.activityId)
            is ActivityViewIntent.SelectAnswer -> onSelectAnswer(intent.answer)
            is ActivityViewIntent.CheckAnswer -> onCheckAnswer()
            is ActivityViewIntent.Next -> onNext()
            is ActivityViewIntent.Finish -> onFinish()
            is ActivityViewIntent.MessageShown -> onMessageShown(intent.uiMessage)
        }
    }

    private fun onLoadActivity(activityId: String) = viewModelScope.launch {
        activityRepository.getQuestions(activityId)
            .onStart { setLoadingState() }
            .catch { error -> setErrorState(error) }
            .collect { questions -> onActivityLoaded(activityId, questions) }
    }

    private fun onActivityLoaded(activityId: String, questions: List<Question>) =
        _uiState.update {
            when {
                questions.isNotEmpty() -> ActivityViewState.Loaded(
                    activityId = activityId,
                    questions = questions,
                    currentQuestion = questions.first(),
                    progress = 0f,
                )
                else -> ActivityViewState.Error(Throwable("No questions found"))
            }
        }

    private fun onSelectAnswer(answer: String) = _uiState.updateLoadedState { state ->
        state.copy(selectedAnswer = answer)
    }

    private fun onCheckAnswer() = _uiState.updateLoadedState { state ->
        val isCorrectAnswer = state.selectedAnswer == state.currentQuestion.correctAnswer
        val attemptsLeft = if (isCorrectAnswer) state.attemptsLeft else state.attemptsLeft - 1
        val points = if (isCorrectAnswer) state.points.inc() else state.points
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

    private fun onNext() = _uiState.updateLoadedState { state ->
        val hasAttemptsRemaining = state.attemptsLeft > 0
        val currentQuestionIndex = state.questions.indexOf(state.currentQuestion)
        val hasNextQuestion = currentQuestionIndex < state.questions.size - 1

        if (hasAttemptsRemaining && hasNextQuestion) {
            val nextQuestionIndex = currentQuestionIndex.inc()
            val nextQuestion = state.questions[nextQuestionIndex]
            val progress = nextQuestionIndex.inc().toFloat().div(state.questions.size)
            state.copy(
                currentQuestion = nextQuestion,
                selectedAnswer = null,
                isResultShown = false,
                progress = progress,
            )
        } else {
            if (hasAttemptsRemaining) {
                completeActivity(state.activityId)
                incrementUserXP(state.points)
            }
            state.copy(
                isFinished = true,
                isSucceeded = hasAttemptsRemaining,
            )
        }
    }

    private fun onFinish() = viewModelScope.launch {
        _uiEffect.send(ActivityViewEffect.NavigateToHome)
    }

    private fun onMessageShown(uiMessage: UIMessage) = _uiState.updateLoadedState { state ->
        state.copy(userMessages = state.userMessages - uiMessage)
    }

    private fun completeActivity(activityId: String) = viewModelScope.launch {
        activityRepository.completeActivity(activityId)
    }

    private fun incrementUserXP(points: Int) = viewModelScope.launch {
        userRepository.addXP(points)
    }

    private fun setErrorState(error: Throwable) =
        _uiState.update { ActivityViewState.Error(error.also { it.printStackTrace() }) }

    private fun setLoadingState() = _uiState.update { ActivityViewState.Loading }
}

private fun MutableStateFlow<ActivityViewState>.updateLoadedState(
    block: (ActivityViewState.Loaded) -> ActivityViewState
) = this.update { state -> (state as? ActivityViewState.Loaded)?.let { block(it) } ?: state }