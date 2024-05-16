package br.com.jwar.triviachallenge.utils

import br.com.jwar.triviachallenge.domain.model.Activity
import br.com.jwar.triviachallenge.domain.model.Question
import br.com.jwar.triviachallenge.domain.model.Unit

object FakeFactory {
    fun makeQuestion(
        id: String = "id",
        activityId: String = "activityId",
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
    ) = List(size) { index ->
        makeQuestion(id = "${index + 1}")
    }

    fun makeUnit(
        id: String = "unitId",
        name: String = "unitName",
        isUnlocked: Boolean = true,
    ) = Unit(
        id = id,
        name = name,
        isUnlocked = isUnlocked,
    )

    fun makeUnitsList(
        size: Int = 3,
    ) = List(size) { index ->
        makeUnit(id = "${index + 1}")
    }

    fun makeActivity(
        id: String = "activityId",
        unitId: String = "unitId",
        name: String = "activityName",
        questions: List<Question> = makeQuestionsList(),
        isUnlocked: Boolean = true,
        isCompleted: Boolean = false,
    ) = Activity(
        id = id,
        unitId = unitId,
        name = name,
        questions = questions,
        isUnlocked = isUnlocked,
        isCompleted = isCompleted,
    )

    fun makeActivitiesList(
        size: Int = 3,
    ) = List(size) { index ->
        makeActivity(id = "${index + 1}")
    }
}