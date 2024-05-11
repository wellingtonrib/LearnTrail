package br.com.jwar.triviachallenge.data.datasources.remote.fake

import br.com.jwar.triviachallenge.data.datasources.remote.RemoteDataSource
import br.com.jwar.triviachallenge.domain.model.Activity
import br.com.jwar.triviachallenge.domain.model.Question
import br.com.jwar.triviachallenge.domain.model.Unit
import javax.inject.Inject

class FakeRemoteDataSource @Inject constructor() : RemoteDataSource {
    override suspend fun getUnits(): List<Unit> {
        return listOf(
            Unit(
                id = "1",
                name = "Unit 1",
            ),
        )
    }

    override suspend fun getQuestions(activityId: String) = listOf(
        Question(
            id = "1",
            activityId = activityId,
            correctAnswer = "2",
            difficulty = "easy",
            answers = listOf("1", "2", "3", "4", "5", "6"),
            question = "What is 1 + 1?",
            type = "multiple",
        ),
        Question(
            id = "2",
            activityId = activityId,
            correctAnswer = "4",
            difficulty = "easy",
            answers = listOf("1", "2", "3", "4", "5", "6"),
            question = "What is 2 + 2?",
            type = "multiple",
        ),
        Question(
            id = "3",
            activityId = activityId,
            correctAnswer = "6",
            difficulty = "easy",
            answers = listOf("1", "2", "3", "4", "5", "6"),
            question = "What is 3 + 3?",
            type = "multiple",
        ),
    )

    override suspend fun getActivities(unitId: String): List<Activity> {
        return listOf(
            Activity("1", "Lesson 1", "1"),
            Activity("2", "Lesson 2", "1"),
            Activity("3", "Lesson 3", "1"),
        )
    }
}