package br.com.jwar.learntrail.data.datasources.remote.opentdb

import br.com.jwar.learntrail.data.datasources.remote.opentdb.dto.OpenTDBQuestionsResponse
import retrofit2.http.GET
import retrofit2.http.Query

const val OPEN_TDB_API_HOST = "https://opentdb.com/"

interface OpenTDBApi {
    @GET("api.php?amount=5&type=multiple")
    suspend fun getQuestions(
        @Query("category") unitId: String,
        @Query("difficulty") activityId: String,
    ): OpenTDBQuestionsResponse
}