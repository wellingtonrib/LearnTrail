package br.com.jwar.triviachallenge.data.datasources.trivia

import br.com.jwar.triviachallenge.data.datasources.trivia.dto.TriviaQuestionsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface TriviaApi {
    @GET("api.php?amount=5&type=multiple")
    suspend fun getQuestions(
        @Query("category") unitId: String,
        @Query("difficulty") activityId: String,
    ): TriviaQuestionsResponse
}