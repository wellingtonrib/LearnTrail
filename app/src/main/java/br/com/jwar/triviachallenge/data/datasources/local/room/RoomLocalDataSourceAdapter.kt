package br.com.jwar.triviachallenge.data.datasources.local.room

import br.com.jwar.triviachallenge.data.datasources.local.room.entities.LessonEntity
import br.com.jwar.triviachallenge.data.datasources.local.room.entities.QuestionEntity
import br.com.jwar.triviachallenge.data.datasources.local.room.entities.UnitEntity
import br.com.jwar.triviachallenge.domain.model.Activity
import br.com.jwar.triviachallenge.domain.model.Lesson
import br.com.jwar.triviachallenge.domain.model.Question
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
            unitId = data.unitId,
        )

    fun adaptFromUnit(unit: Unit) =
        UnitEntity(
            id = unit.id,
            name = unit.name,
        )

    fun adaptFromLesson(lesson: Lesson, unitId: String) =
        LessonEntity(
            id = lesson.id,
            name = lesson.name,
            unitId = unitId
        )

    fun adaptToActivity(questions: List<QuestionEntity>, lessonId: String) =
        Activity(
            lessonId = lessonId,
            questions = questions.map { question ->
                Question(
                    id = question.id,
                    unit = question.unit,
                    correctAnswer = question.correctAnswer,
                    difficulty = question.difficulty,
                    answers = question.answers,
                    question = question.question,
                    type = question.type
                )
            }
        )

    fun adaptFromQuestion(question: Question, lessonId: String) =
        QuestionEntity(
            id = question.id,
            lessonId = lessonId,
            unit = question.unit,
            correctAnswer = question.correctAnswer,
            difficulty = question.difficulty,
            answers = question.answers,
            question = question.question,
            type = question.type,
        )
}