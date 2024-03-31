package br.com.jwar.triviachallenge.data.datasources.local.room

import br.com.jwar.triviachallenge.data.datasources.local.LocalDataSourceStrategy
import br.com.jwar.triviachallenge.data.datasources.local.room.dao.ActivityDao
import br.com.jwar.triviachallenge.data.datasources.local.room.dao.QuestionDao
import br.com.jwar.triviachallenge.data.datasources.local.room.dao.UnitDao
import br.com.jwar.triviachallenge.domain.model.Activity
import br.com.jwar.triviachallenge.domain.model.Unit
import javax.inject.Inject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

class RoomLocalDataSource @Inject constructor(
    private val unitDao: UnitDao,
    private val questionDao: QuestionDao,
    private val activityDao: ActivityDao,
    private val roomAdapter: RoomLocalDataSourceAdapter,
): LocalDataSourceStrategy {
    override suspend fun getUnits() =
        unitDao.getAll().map { units ->
            units.map { unit ->
                val activityEntities = activityDao.getByUnitId(unit.id).firstOrNull().orEmpty()
                roomAdapter.adaptToUnit(unit, activityEntities)
            }
        }

    override suspend fun saveUnits(units: List<Unit>) =
        units.forEach { unit ->
            insertOrUpdateUnit(unit)
            insertActivities(unit.activities, unit.id)
        }

    override suspend fun getActivity(activityId: String) =
        activityDao.getById(activityId).first().let { activityEntity ->
            questionDao.getByActivityId(activityId).map { questions ->
                roomAdapter.adaptToActivity(questions, activityEntity)
            }
        }

    override suspend fun saveActivity(activity: Activity, activityId: String) {
        activity.questions.forEach { question ->
            val questionEntity = roomAdapter.adaptFromQuestion(question, activityId)
            questionDao.insert(questionEntity)
        }
    }

    override suspend fun completeActivity(activityId: String) {
        val activity = activityDao.getById(activityId).first()
        activityDao.updateActivity(activity.copy(isCompleted = true))
    }

    override suspend fun unlockActivity(activityId: String) {
        val activity = activityDao.getById(activityId).first()
        activityDao.updateActivity(activity.copy(isUnlocked = true))
    }

    override suspend fun unlockUnit(unitId: String) {
        val unit = unitDao.getById(unitId).first()
        unitDao.update(unit.copy(isUnlocked = true))
    }

    private suspend fun insertOrUpdateUnit(unit: Unit) {
        val unitEntity = roomAdapter.adaptFromUnit(unit)
        unitDao.getById(unit.id).firstOrNull()?.let {
            unitDao.update(unitEntity)
        } ?: kotlin.run {
            unitDao.insert(unitEntity)
        }
    }

    private suspend fun insertActivities(activities: List<Activity>, unitId: String) {
        val activityEntities = activities.map { activity ->
            roomAdapter.adaptFromActivity(activity, unitId)
        }
        activityDao.insertAll(activityEntities)
    }
}