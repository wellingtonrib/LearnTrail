package br.com.jwar.learntrail.presentation.screens.activity

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import br.com.jwar.learntrail.domain.model.Question
import br.com.jwar.learntrail.presentation.utils.UIMessage

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
        val progress: Float,
        val isFinished: Boolean = false,
        val isSucceeded: Boolean = false,
        val userMessages: List<UIMessage> = emptyList(),
    ) : ActivityViewState() {
        @Composable
        fun getAnswerColor(answer: String) = if (isResultShown) {
            if (answer == currentQuestion.correctAnswer) MaterialTheme.colorScheme.tertiary
            else if (selectedAnswer == answer) MaterialTheme.colorScheme.error
            else Color.Transparent
        } else {
            if (selectedAnswer == answer) MaterialTheme.colorScheme.primary else Color.Transparent
        }

        fun getAnswerTextStyle(answer: String) =
            if (currentQuestion.correctAnswer == answer) FontWeight.Bold
            else FontWeight.Normal
    }
    data class Error(val error: Throwable) : ActivityViewState()

    fun asLoaded() = this as? Loaded
}