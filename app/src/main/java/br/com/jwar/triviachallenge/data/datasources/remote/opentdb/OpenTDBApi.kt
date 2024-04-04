package br.com.jwar.triviachallenge.data.datasources.remote.opentdb

import br.com.jwar.triviachallenge.data.datasources.remote.opentdb.dto.OpenTDBQuestionsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenTDBApi {
    @GET("api.php?amount=5&type=multiple")
    suspend fun getQuestions(
        @Query("category") unitId: String,
        @Query("difficulty") activityId: String,
    ): OpenTDBQuestionsResponse
}