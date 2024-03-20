package br.com.jwar.triviachallenge.data.datasources.remote.trivia

import br.com.jwar.triviachallenge.data.datasources.remote.RemoteDataSourceAdapter
import br.com.jwar.triviachallenge.domain.model.Unit
import br.com.jwar.triviachallenge.data.datasources.remote.trivia.dto.TriviaCategoryResponse
import br.com.jwar.triviachallenge.data.datasources.remote.trivia.dto.TriviaQuestionResult
import br.com.jwar.triviachallenge.data.datasources.remote.trivia.dto.TriviaQuestionsResponse
import br.com.jwar.triviachallenge.data.services.translator.TranslatorService
import br.com.jwar.triviachallenge.domain.model.Activity
import br.com.jwar.triviachallenge.domain.model.Lesson
import br.com.jwar.triviachallenge.domain.model.Question
import javax.inject.Inject

class TriviaRemoteDataSourceAdapter @Inject constructor(
    private val translatorService: TranslatorService,
) : RemoteDataSourceAdapter {

    override suspend fun adaptToUnit(data: Any) = (data as TriviaCategoryResponse).let { data ->
        Unit(
            id = data.id,
            name = data.name.translated(),
            lessons = getLessons()
        )
    }

    override suspend fun adaptToActivity(data: Any) = Activity(
        questions = (data as TriviaQuestionsResponse).results.map { result ->
            Question(
                unit = result.category.translated(),
                correctAnswer = result.correctAnswer.translated(),
                difficulty = result.difficulty,
                answers = result.getAnswers(),
                question = result.question.translated(),
                type = result.type
            )
        }
    )

    private suspend fun TriviaQuestionResult.getAnswers() =
        (incorrectAnswers + correctAnswer).shuffled().map { answer ->
            answer.translated()
        }

    private suspend fun getLessons() = listOf(
        Lesson(id = "easy", name = "Easy".translated()),
        Lesson(id = "medium", name = "Medium".translated()),
        Lesson(id = "hard", name = "Difficult".translated())
    )

    private suspend fun String.translated() = translatorService.translate(this)
}