package br.com.jwar.triviachallenge.data.datasources.local.room

import br.com.jwar.triviachallenge.data.datasources.local.room.entities.ActivityEntity
import br.com.jwar.triviachallenge.data.datasources.local.room.entities.QuestionEntity
import br.com.jwar.triviachallenge.data.datasources.local.room.entities.UnitEntity
import br.com.jwar.triviachallenge.domain.model.Activity
import br.com.jwar.triviachallenge.domain.model.Question
import br.com.jwar.triviachallenge.domain.model.Unit
import javax.inject.Inject

class RoomLocalDataSourceAdapter @Inject constructor() {

    fun adaptToUnit(entity: UnitEntity) =
        Unit(
            id = entity.id,
            name = entity.name,
            isUnlocked = entity.isUnlocked,
        )

    fun adaptFromUnit(unit: Unit) =
        UnitEntity(
            id = unit.id,
            name = unit.name,
            isUnlocked = unit.isUnlocked,
        )

    fun adaptFromActivity(activity: Activity) =
        ActivityEntity(
            id = activity.id,
            name = activity.name,
            unitId = activity.unitId,
            isUnlocked = activity.isUnlocked,
            isCompleted = activity.isCompleted,
        )

    fun adaptToActivity(entity: ActivityEntity) =
        Activity(
            id = entity.id,
            name = entity.name,
            unitId = entity.unitId,
            isUnlocked = entity.isUnlocked,
            isCompleted = entity.isCompleted,
        )

    fun adaptToQuestions(entities: List<QuestionEntity>) =
        entities.map { entity ->
            Question(
                id = entity.id,
                activityId = entity.activityId,
                correctAnswer = entity.correctAnswer,
                difficulty = entity.difficulty,
                answers = entity.answers,
                question = entity.question,
                type = entity.type,
            )
        }

    fun adaptFromQuestion(question: Question) =
        QuestionEntity(
            id = question.id,
            activityId = question.activityId,
            correctAnswer = question.correctAnswer,
            difficulty = question.difficulty,
            answers = question.answers,
            question = question.question,
            type = question.type,
        )
}