package br.com.jwar.triviachallenge.data.datasources.remote.opentdb

import br.com.jwar.triviachallenge.data.datasources.remote.opentdb.dto.OpenTDBCategoryResponse
import br.com.jwar.triviachallenge.data.datasources.remote.opentdb.dto.OpenTDBQuestionResult
import br.com.jwar.triviachallenge.data.datasources.remote.opentdb.dto.OpenTDBQuestionsResponse
import br.com.jwar.triviachallenge.data.services.translator.TranslatorService
import br.com.jwar.triviachallenge.domain.model.Activity
import br.com.jwar.triviachallenge.domain.model.Question
import br.com.jwar.triviachallenge.domain.model.Unit
import javax.inject.Inject

class OpenTDBRemoteDataSourceAdapter @Inject constructor(
    private val translatorService: TranslatorService,
) {
    suspend fun adaptToUnit(data: OpenTDBCategoryResponse) =
        Unit(
            id = data.id,
            name = data.name.translated()
        )

    suspend fun adaptToActivity(activity: Activity) = activity.copy(
        name = activity.name.translated()
    )

    suspend fun adaptToQuestions(
        data: OpenTDBQuestionsResponse,
        activityId: String
    ) = data.results.mapIndexed { index, result ->
        Question(
            id = "$activityId:$index",
            activityId = activityId,
            correctAnswer = result.correctAnswer.translated(),
            difficulty = result.difficulty,
            answers = result.getAnswers(),
            question = result.question.translated(),
            type = result.type
        )
    }

    private suspend fun OpenTDBQuestionResult.getAnswers() =
        (incorrectAnswers + correctAnswer).shuffled().map { answer ->
            answer.translated()
        }
    private suspend fun String.translated() = translatorService.translate(this)
}