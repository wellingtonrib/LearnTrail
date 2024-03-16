package br.com.jwar.triviachallenge.data.datasources.dto

import com.squareup.moshi.Json

data class TriviaQuestionsResponse(
    @field:Json(name = "response_code") val responseCode: Int,
    @field:Json(name = "results") val results: List<TriviaQuestionResult>
)

