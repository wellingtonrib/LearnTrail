package br.com.jwar.triviachallenge.data.services

import br.com.jwar.triviachallenge.data.services.responses.ActivityResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ActivityService {
    @GET("api.php?amount=5&type=multiple")
    suspend fun getActivity(
        @Query("category") unitId: String,
        @Query("difficulty") activityId: String,
    ): ActivityResponse
}