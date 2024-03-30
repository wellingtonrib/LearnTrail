package br.com.jwar.triviachallenge.data.datasources.local.room

import br.com.jwar.triviachallenge.data.datasources.local.LocalDataSourceStrategy
import br.com.jwar.triviachallenge.data.datasources.local.room.dao.LessonDao
import br.com.jwar.triviachallenge.data.datasources.local.room.dao.QuestionDao
import br.com.jwar.triviachallenge.data.datasources.local.room.dao.UnitDao
import br.com.jwar.triviachallenge.domain.model.Activity
import br.com.jwar.triviachallenge.domain.model.Unit
import javax.inject.Inject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class RoomLocalDataSource @Inject constructor(
    private val unitDao: UnitDao,
    private val questionDao: QuestionDao,
    private val lessonDao: LessonDao,
    private val roomAdapter: RoomLocalDataSourceAdapter,
): LocalDataSourceStrategy {
    override suspend fun getUnits() =
        unitDao.getAll().map { units ->
            units.map { unit ->
                val lessons = lessonDao.getByUnitId(unit.id).first()
                roomAdapter.adaptToUnit(unit, lessons)
            }
        }

    override suspend fun saveUnits(units: List<Unit>) =
        units.forEach { unit ->
            val unitEntity = roomAdapter.adaptFromUnit(unit)
            unitDao.insert(unitEntity)
            val lessonEntities = unit.lessons.map { lesson ->
                roomAdapter.adaptFromLesson(lesson, unit.id)
            }
            lessonDao.insertAll(lessonEntities)
        }

    override suspend fun getActivity(lessonId: String) =
        questionDao.getByLessonId(lessonId).map { questions ->
            roomAdapter.adaptToActivity(questions, lessonId)
        }

    override suspend fun saveActivity(activity: Activity, lessonId: String) {
        activity.questions.forEach { question ->
            val questionEntity = roomAdapter.adaptFromQuestion(question, lessonId)
            questionDao.insert(questionEntity)
        }
    }
}