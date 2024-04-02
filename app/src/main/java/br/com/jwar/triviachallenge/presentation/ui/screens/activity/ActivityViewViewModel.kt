package br.com.jwar.triviachallenge.presentation.ui.screens.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.jwar.triviachallenge.domain.model.Question
import br.com.jwar.triviachallenge.domain.repositories.ActivityRepository
import br.com.jwar.triviachallenge.presentation.utils.UIMessage
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
import kotlinx.coroutines.CoroutineScope

@HiltViewModel
class ActivityViewViewModel @Inject constructor(
    override val activityRepository: ActivityRepository
): ViewModel(), ActivityViewProcessor {

    override val scope: CoroutineScope = viewModelScope

    private val _uiState = MutableStateFlow<ActivityViewState>(ActivityViewState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _uiEffect = Channel<ActivityViewEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()

    fun getActivity(activityId: String) = viewModelScope.launch {
        activityRepository.getQuestions(activityId)
            .onStart { setLoadingState() }
            .catch { error -> setErrorState(error) }
            .collect { questions -> setLoadedState(activityId, questions) }
    }

    private fun setLoadedState(activityId: String, questions: List<Question>) {
        _uiState.update {
            ActivityViewState.Loaded(
                activityId = activityId,
                questions = questions,
                currentQuestion = questions.first(),
                progress = "1/${questions.size}",
            )
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
            getStateByCheckingAnswer(state)
        }
    }

    fun onNext() {
        _uiState.updateLoadedState { state ->
            getStateByCheckingNextQuestion(state)
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