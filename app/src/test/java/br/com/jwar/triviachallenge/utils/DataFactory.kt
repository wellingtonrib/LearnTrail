package br.com.jwar.triviachallenge.utils

import br.com.jwar.triviachallenge.domain.model.Activity
import br.com.jwar.triviachallenge.domain.model.Question
import br.com.jwar.triviachallenge.domain.model.Unit
import java.util.UUID

object DataFactory {
    fun makeUnit(
        id: String = UUID.randomUUID().toString(),
        name: String = "Unit $id",
        isUnlocked: Boolean = true,
    ) = Unit(
        id = id,
        name = name,
        isUnlocked = isUnlocked,
    )

    fun makeUnitsList(
        size: Int = 3,
        builder: (Int) -> Unit = { makeUnit() },
    ) = List(size) { index -> builder(index) }

    fun makeActivity(
        id: String = UUID.randomUUID().toString(),
        unitId: String = UUID.randomUUID().toString(),
        name: String = "Activity $id",
        isUnlocked: Boolean = true,
        isCompleted: Boolean = false,
    ) = Activity(
        id = id,
        name = name,
        unitId = unitId,
        isUnlocked = isUnlocked,
        isCompleted = isCompleted,
    )

    fun makeActivitiesList(
        size: Int = 3,
        builder: (Int) -> Activity = { makeActivity() },
    ) = List(size) { index -> builder(index) }

    fun makeQuestion(
        id: String = UUID.randomUUID().toString(),
        activityId: String = UUID.randomUUID().toString(),
        question: String = "question",
        answers: List<String> = listOf("correctAnswer", "wrongAnswer"),
        correctAnswer: String = "correctAnswer",
        difficulty: String = "easy",
        type: String = "multiple",
    ) = Question(
        id = id,
        activityId = activityId,
        question = question,
        answers = answers,
        correctAnswer = correctAnswer,
        difficulty = difficulty,
        type = type,
    )

    fun makeQuestionsList(
        size: Int = 3,
        builder: (Int) -> Question = { makeQuestion() },
    ) = List(size) { index -> builder(index) }
}