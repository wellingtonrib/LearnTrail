package br.com.jwar.triviachallenge.data.datasources.local.database.room

import br.com.jwar.triviachallenge.data.datasources.local.database.LocalDataSource
import br.com.jwar.triviachallenge.data.datasources.local.database.room.dao.ActivityDao
import br.com.jwar.triviachallenge.data.datasources.local.database.room.dao.QuestionDao
import br.com.jwar.triviachallenge.data.datasources.local.database.room.dao.UnitDao
import br.com.jwar.triviachallenge.domain.model.Activity
import br.com.jwar.triviachallenge.domain.model.Question
import br.com.jwar.triviachallenge.domain.model.Unit
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

class RoomLocalDataSource @Inject constructor(
    private val unitDao: UnitDao,
    private val questionDao: QuestionDao,
    private val activityDao: ActivityDao,
    private val roomAdapter: RoomLocalDataSourceAdapter,
): LocalDataSource {
    override suspend fun getUnits() =
        unitDao.getAll().map { unitEntities ->
            unitEntities.map { unit ->
                roomAdapter.adaptToUnit(unit)
            }
        }

    override suspend fun getUnit(unitId: String) =
        unitDao.findById(unitId).map { unitEntity ->
            roomAdapter.adaptToUnit(unitEntity)
        }

    override suspend fun saveUnits(units: List<Unit>) =
        units.forEach { unit ->
            val unitEntity = roomAdapter.adaptFromUnit(unit)
            unitDao.findById(unit.id).firstOrNull()?.let {
                unitDao.update(unitEntity)
            } ?: kotlin.run {
                unitDao.insert(unitEntity)
            }
        }

    override suspend fun getQuestions(activityId: String): Flow<List<Question>> =
        questionDao.findByActivityId(activityId).map { questionEntities ->
            roomAdapter.adaptToQuestions(questionEntities)
        }

    override suspend fun getActivity(activityId: String) =
        activityDao.findById(activityId).map { activityEntity ->
            roomAdapter.adaptToActivity(activityEntity)
        }

    override suspend fun getActivities(unitId: String) =
        activityDao.findByUnitId(unitId).map { activityEntities ->
            activityEntities.map { activityEntity ->
                roomAdapter.adaptToActivity(activityEntity)
            }
        }

    override suspend fun saveActivities(activities: List<Activity>) =
        activities.forEach { activity ->
            val activityEntity = roomAdapter.adaptFromActivity(activity)
            activityDao.findById(activity.id).firstOrNull()?.let {
                activityDao.update(activityEntity.id, activityEntity.name, activityEntity.unitId)
            } ?: kotlin.run {
                activityDao.insert(activityEntity)
            }
        }

    override suspend fun saveQuestions(questions: List<Question>) {
        val questionEntities = questions.map { question -> roomAdapter.adaptFromQuestion(question) }
        questionDao.insertAll(questionEntities)
    }

    override suspend fun updateActivity(activity: Activity) {
        val activityEntity = roomAdapter.adaptFromActivity(activity)
        activityDao.update(activityEntity)
    }

    override suspend fun updateUnit(unit: Unit) {
        val unitEntity = roomAdapter.adaptFromUnit(unit)
        unitDao.update(unitEntity)
    }
}