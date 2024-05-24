package br.com.jwar.triviachallenge.data.datasources.remote.fake

import br.com.jwar.triviachallenge.data.datasources.remote.RemoteDataSourceStrategy
import br.com.jwar.triviachallenge.domain.model.Activity
import br.com.jwar.triviachallenge.domain.model.Lesson
import br.com.jwar.triviachallenge.domain.model.Question
import br.com.jwar.triviachallenge.domain.model.Unit
import javax.inject.Inject

class FakeRemoteDataSource @Inject constructor() : RemoteDataSourceStrategy {
    override suspend fun getUnits(): List<Unit> {
        return listOf(
            Unit(
                id = "1",
                name = "Unit 1",
                lessons = listOf(
                    Lesson("1", "Lesson 1"),
                    Lesson("2", "Lesson 2"),
                    Lesson("3", "Lesson 3"),
                ),
            ),
        )
    }

    override suspend fun getActivity(lessonId: String) = Activity(
        questions = listOf(
            Question(
                id = "1",
                unit = "Unit 1",
                correctAnswer = "2",
                difficulty = "easy",
                answers = listOf("1", "2", "3", "4", "5", "6"),
                question = "What is 1 + 1?",
                type = "multiple",
            ),
            Question(
                id = "2",
                unit = "Unit 1",
                correctAnswer = "4",
                difficulty = "easy",
                answers = listOf("1", "2", "3", "4", "5", "6"),
                question = "What is 2 + 2?",
                type = "multiple",
            ),
            Question(
                id = "3",
                unit = "Unit 1",
                correctAnswer = "6",
                difficulty = "easy",
                answers = listOf("1", "2", "3", "4", "5", "6"),
                question = "What is 3 + 3?",
                type = "multiple",
            ),
        ),
    )
}