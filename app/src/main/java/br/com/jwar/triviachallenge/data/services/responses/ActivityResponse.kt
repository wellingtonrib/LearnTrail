package br.com.jwar.triviachallenge.data.services.responses

import com.squareup.moshi.Json

data class ActivityResponse(
    @field:Json(name = "response_code") val responseCode: Int,
    @field:Json(name = "results") val results: List<Result>
)

data class Result(
    @field:Json(name = "category") val category: String,
    @field:Json(name = "correct_answer") val correctAnswer: String,
    @field:Json(name = "difficulty") val difficulty: String,
    @field:Json(name = "incorrect_answers") val incorrectAnswers: List<String>,
    @field:Json(name = "question") val question: String,
    @field:Json(name = "type") val type: String
)