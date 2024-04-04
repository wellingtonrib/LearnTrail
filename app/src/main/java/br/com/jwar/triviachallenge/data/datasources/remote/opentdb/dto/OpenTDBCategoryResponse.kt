package br.com.jwar.triviachallenge.data.datasources.remote.opentdb.dto

import com.squareup.moshi.Json

data class OpenTDBCategoryResponse(
    @field:Json(name = "id") val id: String,
    @field:Json(name = "name") val name: String,
)

