package br.com.jwar.learntrail.data.datasources.local.database

import br.com.jwar.learntrail.domain.model.Activity
import br.com.jwar.learntrail.domain.model.Question
import br.com.jwar.learntrail.domain.model.Unit
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
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