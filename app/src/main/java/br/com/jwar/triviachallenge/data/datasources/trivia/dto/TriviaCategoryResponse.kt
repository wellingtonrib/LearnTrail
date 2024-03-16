package br.com.jwar.triviachallenge.data.datasources.trivia.dto

import com.squareup.moshi.Json

data class TriviaCategoryResponse(
    @field:Json(name = "id") val id: String,
    @field:Json(name = "name") val name: String,
)

