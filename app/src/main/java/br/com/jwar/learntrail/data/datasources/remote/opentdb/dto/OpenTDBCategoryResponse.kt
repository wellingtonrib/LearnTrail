package br.com.jwar.learntrail.data.datasources.remote.opentdb.dto

import com.squareup.moshi.Json

data class OpenTDBCategoryResponse(
    @Json(name = "id") val id: String,
    @Json(name = "name") val name: String,
)

