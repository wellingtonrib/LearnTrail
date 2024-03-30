package br.com.jwar.triviachallenge.data.datasources.remote.trivia

import br.com.jwar.triviachallenge.domain.model.Unit
import br.com.jwar.triviachallenge.data.datasources.remote.trivia.dto.TriviaCategoryResponse
import br.com.jwar.triviachallenge.data.datasources.remote.trivia.dto.TriviaQuestionResult
import br.com.jwar.triviachallenge.data.datasources.remote.trivia.dto.TriviaQuestionsResponse
import br.com.jwar.triviachallenge.data.services.translator.TranslatorService
import br.com.jwar.triviachallenge.domain.model.Activity
import br.com.jwar.triviachallenge.domain.model.Lesson
import br.com.jwar.triviachallenge.domain.model.Question
import java.util.UUID
import javax.inject.Inject

class TriviaRemoteDataSourceAdapter @Inject constructor(
    private val translatorService: TranslatorService,
) {
    suspend fun adaptToUnit(data: Any) =
        (data as TriviaCategoryResponse).let {
            Unit(
                id = data.id,
                name = data.name.translated(),
                lessons = getLessons(data.id)
            )
        }

    suspend fun adaptToActivity(data: Any, lessonId: String) = Activity(
        lessonId = lessonId,
        questions = (data as TriviaQuestionsResponse).results.map { result ->
            Question(
                id = UUID.randomUUID().toString(),
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

    private suspend fun getLessons(unitId: String) = listOf(
        Lesson(id = "$unitId:easy", name = "Easy".translated(), unitId = unitId),
        Lesson(id = "$unitId:medium", name = "Medium".translated(), unitId = unitId),
        Lesson(id = "$unitId:hard", name = "Difficult".translated(), unitId = unitId)
    )

    private suspend fun String.translated() = translatorService.translate(this)
}