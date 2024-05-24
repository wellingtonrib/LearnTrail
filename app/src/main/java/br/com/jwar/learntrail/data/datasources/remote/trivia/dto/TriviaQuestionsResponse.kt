package br.com.jwar.learntrail.data.datasources.remote.trivia.dto

import com.squareup.moshi.Json

data class TriviaQuestionsResponse(
    @field:Json(name = "response_code") val responseCode: Int,
    @field:Json(name = "results") val results: List<TriviaQuestionResult>
)

