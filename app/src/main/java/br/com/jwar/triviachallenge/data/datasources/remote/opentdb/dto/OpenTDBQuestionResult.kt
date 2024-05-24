package br.com.jwar.triviachallenge.data.datasources.remote.opentdb.dto

import com.squareup.moshi.Json

data class OpenTDBQuestionResult(
    @Json(name = "category") val category: String,
    @Json(name = "correct_answer") val correctAnswer: String,
    @Json(name = "difficulty") val difficulty: String,
    @Json(name = "incorrect_answers") val incorrectAnswers: List<String>,
    @Json(name = "question") val question: String,
    @Json(name = "type") val type: String
)