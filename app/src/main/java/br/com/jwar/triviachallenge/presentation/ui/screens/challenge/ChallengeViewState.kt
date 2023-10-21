package br.com.jwar.triviachallenge.presentation.ui.screens.challenge

import br.com.jwar.triviachallenge.domain.model.Challenge
import br.com.jwar.triviachallenge.domain.model.Question

sealed class ChallengeViewState {

    object Loading : ChallengeViewState()

    data class Loaded(
        val challenge: Challenge,
        val nextQuestion: Question = challenge.questions.first(),
        val selectedAnswer: String? = null,
        val isResultShown: Boolean = false,
        val isLastQuestion: Boolean = challenge.questions.last() == nextQuestion
    ) : ChallengeViewState()

    data class Error(val error: Throwable) : ChallengeViewState()
}