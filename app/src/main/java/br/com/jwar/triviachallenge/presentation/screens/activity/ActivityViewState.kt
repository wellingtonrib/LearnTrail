package br.com.jwar.triviachallenge.presentation.screens.activity

import br.com.jwar.triviachallenge.domain.model.Question
import br.com.jwar.triviachallenge.presentation.utils.UIMessage

sealed class ActivityViewState {
    object Loading : ActivityViewState()
    data class Loaded(
        val activityId: String,
        val questions: List<Question>,
        val currentQuestion: Question,
        val selectedAnswer: String? = null,
        val isResultShown: Boolean = false,
        val attemptsLeft: Int = 3,
        val points: Int = 0,
        val progress: String,
        val isFinished: Boolean = false,
        val isSucceeded: Boolean = false,
        val userMessages: List<UIMessage> = emptyList(),
    ) : ActivityViewState()
    data class Error(val error: Throwable) : ActivityViewState()

    sealed class Action {
        data class OnLoaded(val activityId: String, val questions: List<Question>) : Action()
        object OnCheck : Action()
        object OnNext : Action()
    }
}