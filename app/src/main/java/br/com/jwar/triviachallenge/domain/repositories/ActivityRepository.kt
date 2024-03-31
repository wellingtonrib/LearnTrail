package br.com.jwar.triviachallenge.domain.repositories

import br.com.jwar.triviachallenge.domain.model.Activity
import kotlinx.coroutines.flow.Flow

interface ActivityRepository {
    fun getActivity(activityId: String): Flow<Activity>
    suspend fun completeActivity(activityId: String)
    suspend fun unlockActivity(id: String)
}