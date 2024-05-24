package br.com.jwar.learntrail.presentation.ui.screens.activity

import br.com.jwar.learntrail.domain.model.Activity
import br.com.jwar.learntrail.domain.model.Question
import br.com.jwar.learntrail.presentation.utils.UIMessage

sealed class ActivityViewState {
    object Loading : ActivityViewState()
    data class Loaded(
        val activity: Activity,
        val currentQuestion: Question,
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