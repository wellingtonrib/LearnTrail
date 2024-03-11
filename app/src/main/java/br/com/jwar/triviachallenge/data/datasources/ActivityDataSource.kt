package br.com.jwar.triviachallenge.data.datasources

import br.com.jwar.triviachallenge.data.services.responses.ActivityResponse

interface ActivityDataSource {
    suspend fun getActivity(unitId: String, activityId: String): ActivityResponse
}