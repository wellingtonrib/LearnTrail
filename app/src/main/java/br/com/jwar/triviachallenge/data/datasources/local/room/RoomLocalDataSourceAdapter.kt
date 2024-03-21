package br.com.jwar.triviachallenge.data.datasources.local.room

import br.com.jwar.triviachallenge.data.datasources.local.room.entities.LessonEntity
import br.com.jwar.triviachallenge.data.datasources.local.room.entities.UnitEntity
import br.com.jwar.triviachallenge.domain.model.Lesson
import br.com.jwar.triviachallenge.domain.model.Unit
import javax.inject.Inject

class RoomLocalDataSourceAdapter @Inject constructor() {

    fun adaptToUnit(entity: Any, lessons: List<LessonEntity>) =
        (entity as UnitEntity).let {
            Unit(
                id = entity.id,
                name = entity.name,
                lessons = lessons.map { lesson -> adaptToLesson(lesson) }
            )
        }

    fun adaptToLesson(data: LessonEntity) =
        Lesson(
            id = data.id,
            name = data.name,
        )

    fun adaptFromUnit(unit: Unit) =
        UnitEntity(
            id = unit.id,
            name = unit.name
        )

    fun adaptFromLesson(lesson: Lesson, unitId: String) =
        LessonEntity(
            id = lesson.id,
            name = lesson.name,
            unitId = unitId
        )
}