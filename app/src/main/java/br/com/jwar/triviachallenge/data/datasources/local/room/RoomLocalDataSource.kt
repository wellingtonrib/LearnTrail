package br.com.jwar.triviachallenge.data.datasources.local.room

import br.com.jwar.triviachallenge.data.datasources.local.LocalDataSourceStrategy
import br.com.jwar.triviachallenge.data.datasources.local.room.dao.UnitDao
import br.com.jwar.triviachallenge.domain.model.Unit
import javax.inject.Inject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class RoomLocalDataSource @Inject constructor(
    private val unitDao: UnitDao,
    private val roomAdapter: RoomLocalDataSourceAdapter,
): LocalDataSourceStrategy {
    override suspend fun getUnits() =
        unitDao.getUnits().map { units ->
            units.map { unit ->
                val lessons = unitDao.getLessons(unit.id).first()
                roomAdapter.adaptToUnit(unit, lessons)
            }
        }

    override suspend fun saveUnits(units: List<Unit>) =
        units.forEach { unit ->
            val unitEntity = roomAdapter.adaptFromUnit(unit)
            unitDao.insertUnits(listOf(unitEntity))

            val lessonEntities = unit.lessons.map { lesson ->
                roomAdapter.adaptFromLesson(lesson, unit.id)
            }
            unitDao.insertLessons(lessonEntities)
        }
}