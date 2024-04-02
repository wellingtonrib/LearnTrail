package br.com.jwar.triviachallenge.data.datasources.local

import br.com.jwar.triviachallenge.domain.model.Activity
import br.com.jwar.triviachallenge.domain.model.Question
import br.com.jwar.triviachallenge.domain.model.Unit
import kotlinx.coroutines.flow.Flow

interface LocalDataSourceStrategy {
    suspend fun getUnit(unitId: String): Flow<Unit>
    suspend fun getUnits(): Flow<List<Unit>>
    suspend fun updateUnit(unit: Unit)
    suspend fun saveUnits(units: List<Unit>)
    suspend fun getActivity(activityId: String): Flow<Activity>
    suspend fun getActivities(unitId: String): Flow<List<Activity>>
    suspend fun saveActivities(activities: List<Activity>)
    suspend fun updateActivity(activity: Activity)
    suspend fun getQuestions(activityId: String): Flow<List<Question>>
    suspend fun saveQuestions(questions: List<Question>)
}