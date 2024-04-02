package br.com.jwar.triviachallenge.domain.repositories

import br.com.jwar.triviachallenge.domain.model.Activity
import br.com.jwar.triviachallenge.domain.model.Question
import kotlinx.coroutines.flow.Flow

interface ActivityRepository {
    suspend fun getActivities(unitId: String): Flow<List<Activity>>
    suspend fun getQuestions(activityId: String): Flow<List<Question>>
    suspend fun completeActivity(activityId: String)
    suspend fun unlockActivity(activityId: String)
}