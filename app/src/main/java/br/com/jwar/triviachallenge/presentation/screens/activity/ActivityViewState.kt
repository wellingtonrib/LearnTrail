package br.com.jwar.triviachallenge.presentation.screens.activity

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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
    ) : ActivityViewState() {
        fun getAnswerColor(answer: String) = if (isResultShown) {
            if (answer == currentQuestion.correctAnswer) Color.Green
            else if (selectedAnswer == answer) Color.Red
            else Color.Transparent
        } else {
            if (selectedAnswer == answer) Color.Blue else Color.Transparent
        }

        fun getAnswerTextStyle(answer: String) =
            if (currentQuestion.correctAnswer == answer) FontWeight.Bold
            else FontWeight.Normal
    }
    data class Error(val error: Throwable) : ActivityViewState()

    fun asLoaded() = this as? Loaded
}