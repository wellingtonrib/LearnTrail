package br.com.jwar.triviachallenge.data.datasources.remote.trivia

import br.com.jwar.triviachallenge.domain.model.Unit
import br.com.jwar.triviachallenge.data.datasources.remote.trivia.dto.TriviaCategoryResponse
import br.com.jwar.triviachallenge.data.datasources.remote.trivia.dto.TriviaQuestionResult
import br.com.jwar.triviachallenge.data.datasources.remote.trivia.dto.TriviaQuestionsResponse
import br.com.jwar.triviachallenge.data.services.translator.TranslatorService
import br.com.jwar.triviachallenge.domain.model.Activity
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
                activities = getActivities(data.id)
            )
        }

    suspend fun adaptToActivity(
        data: Any,
        activityId: String,
        categoryId: String,
        difficult: String
    ) = Activity(
        id = activityId,
        name = difficult.translated(),
        unitId = categoryId,
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

    private suspend fun getActivities(unitId: String) = listOf(
        Activity(id = "$unitId:easy", name = "Easy".translated(), unitId = unitId),
        Activity(id = "$unitId:medium", name = "Medium".translated(), unitId = unitId),
        Activity(id = "$unitId:hard", name = "Difficult".translated(), unitId = unitId)
    )

    private suspend fun String.translated() = translatorService.translate(this)
}