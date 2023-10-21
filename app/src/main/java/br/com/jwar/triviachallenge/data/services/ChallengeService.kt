package br.com.jwar.triviachallenge.data.services

import br.com.jwar.triviachallenge.data.services.responses.ChallengeResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ChallengeService {
    @GET("api.php?amount=5&type=multiple")
    suspend fun getChallenge(@Query("category") categoryId: String): ChallengeResponse
}