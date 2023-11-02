package br.com.jwar.triviachallenge.presentation.ui.screens.challenge

import br.com.jwar.triviachallenge.domain.model.Challenge
import br.com.jwar.triviachallenge.domain.model.Question
import br.com.jwar.triviachallenge.presentation.model.UIMessage

sealed class ChallengeViewState {

    object Loading : ChallengeViewState()

    data class Loaded(
        val challenge: Challenge,
        val currentQuestion: Question = challenge.questions.first(),
        val selectedAnswer: String? = null,
        val isResultShown: Boolean = false,
        val attemptsLeft: Int = 3,
        val points: Int = 0,
        val progress: String = "1/${challenge.questions.size}",
        val isFinished: Boolean = false,
        val isSucceeded: Boolean = false,
        val userMessages: List<UIMessage> = emptyList(),
    ) : ChallengeViewState()

    data class Error(val error: Throwable) : ChallengeViewState()
}