package br.com.jwar.triviachallenge.data.datasources.remote.opentdb.dto

import com.squareup.moshi.Json

data class OpenTDBQuestionsResponse(
    @field:Json(name = "response_code") val responseCode: Int,
    @field:Json(name = "results") val results: List<OpenTDBQuestionResult>
)

