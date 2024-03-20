package br.com.jwar.triviachallenge.data.datasources.remote

import br.com.jwar.triviachallenge.domain.model.Activity
import br.com.jwar.triviachallenge.domain.model.Unit

interface RemoteDataSourceStrategy {
    suspend fun getUnits(): List<Unit>
    suspend fun getActivity(unitId: String, activityId: String): Activity
}