package br.com.jwar.triviachallenge.data.datasources.remote.opentdb.dto

import com.squareup.moshi.Json

data class OpenTDBQuestionsResponse(
    @Json(name = "response_code") val responseCode: Int,
    @Json(name = "results") val results: List<OpenTDBQuestionResult>
)

