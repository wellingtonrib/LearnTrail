package br.com.jwar.triviachallenge.data.datasources

import br.com.jwar.triviachallenge.data.datasources.dto.TriviaQuestionsResponse
import retrofit2.http.GET
import retrofit2.http.Query

const val TRIVIA_API_BASE_URL = "https://opentdb.com/"

interface TriviaApi {
    @GET("api.php?amount=5&type=multiple")
    suspend fun getQuestions(
        @Query("category") unitId: String,
        @Query("difficulty") activityId: String,
    ): TriviaQuestionsResponse
}