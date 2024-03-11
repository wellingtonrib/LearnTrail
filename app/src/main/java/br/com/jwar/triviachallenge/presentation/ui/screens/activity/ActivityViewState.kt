package br.com.jwar.triviachallenge.presentation.ui.screens.activity

import br.com.jwar.triviachallenge.domain.model.Activity
import br.com.jwar.triviachallenge.domain.model.Question
import br.com.jwar.triviachallenge.presentation.utils.UIMessage

sealed class ActivityViewState {
    object Loading : ActivityViewState()
    data class Loaded(
        val activity: Activity,
        val currentQuestion: Question = activity.questions.first(),
        val selectedAnswer: String? = null,
        val isResultShown: Boolean = false,
        val attemptsLeft: Int = 3,
        val points: Int = 0,
        val progress: String = "1/${activity.questions.size}",
        val isFinished: Boolean = false,
        val isSucceeded: Boolean = false,
        val userMessages: List<UIMessage> = emptyList(),
    ) : ActivityViewState()
    data class Error(val error: Throwable) : ActivityViewState()
}