package br.com.jwar.triviachallenge.data.datasources.dto

import com.squareup.moshi.Json

data class TriviaQuestionResult(
    @field:Json(name = "category") val category: String,
    @field:Json(name = "correct_answer") val correctAnswer: String,
    @field:Json(name = "difficulty") val difficulty: String,
    @field:Json(name = "incorrect_answers") val incorrectAnswers: List<String>,
    @field:Json(name = "question") val question: String,
    @field:Json(name = "type") val type: String
)