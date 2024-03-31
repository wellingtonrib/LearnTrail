package br.com.jwar.triviachallenge.data.datasources.local.room

import br.com.jwar.triviachallenge.data.datasources.local.room.entities.ActivityEntity
import br.com.jwar.triviachallenge.data.datasources.local.room.entities.QuestionEntity
import br.com.jwar.triviachallenge.data.datasources.local.room.entities.UnitEntity
import br.com.jwar.triviachallenge.domain.model.Activity
import br.com.jwar.triviachallenge.domain.model.Question
import br.com.jwar.triviachallenge.domain.model.Unit
import javax.inject.Inject

class RoomLocalDataSourceAdapter @Inject constructor() {

    fun adaptToUnit(entity: Any, activityEntities: List<ActivityEntity>) =
        (entity as UnitEntity).let {
            Unit(
                id = entity.id,
                name = entity.name,
                activities = activityEntities.map { activity -> adaptToActivity(activity) },
                isUnlocked = entity.isUnlocked,
            )
        }

    fun adaptToActivity(data: ActivityEntity) =
        Activity(
            id = data.id,
            name = data.name,
            unitId = data.unitId,
            isUnlocked = data.isUnlocked,
            isCompleted = data.isCompleted,
        )

    fun adaptFromUnit(unit: Unit) =
        UnitEntity(
            id = unit.id,
            name = unit.name,
            isUnlocked = unit.isUnlocked,
        )

    fun adaptFromActivity(activity: Activity, unitId: String) =
        ActivityEntity(
            id = activity.id,
            name = activity.name,
            unitId = unitId,
            isUnlocked = activity.isUnlocked,
            isCompleted = activity.isCompleted,
        )

    fun adaptToActivity(questions: List<QuestionEntity>, entity: ActivityEntity) =
        Activity(
            id = entity.id,
            name = entity.name,
            unitId = entity.unitId,
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
            },
            isUnlocked = entity.isUnlocked,
            isCompleted = entity.isCompleted,
        )

    fun adaptFromQuestion(question: Question, activityId: String) =
        QuestionEntity(
            id = question.id,
            activityId = activityId,
            unit = question.unit,
            correctAnswer = question.correctAnswer,
            difficulty = question.difficulty,
            answers = question.answers,
            question = question.question,
            type = question.type,
        )
}