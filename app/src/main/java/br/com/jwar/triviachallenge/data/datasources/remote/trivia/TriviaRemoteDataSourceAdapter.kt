package br.com.jwar.triviachallenge.data.datasources.remote.trivia

import br.com.jwar.triviachallenge.data.datasources.remote.trivia.dto.TriviaCategoryResponse
import br.com.jwar.triviachallenge.data.datasources.remote.trivia.dto.TriviaQuestionResult
import br.com.jwar.triviachallenge.data.datasources.remote.trivia.dto.TriviaQuestionsResponse
import br.com.jwar.triviachallenge.data.services.translator.TranslatorService
import br.com.jwar.triviachallenge.domain.model.Question
import br.com.jwar.triviachallenge.domain.model.Unit
import java.util.UUID
import javax.inject.Inject

class TriviaRemoteDataSourceAdapter @Inject constructor(
    private val translatorService: TranslatorService,
) {
    suspend fun adaptToUnit(data: TriviaCategoryResponse) =
        Unit(
            id = data.id,
            name = data.name.translated()
        )

    suspend fun adaptToQuestions(
        data: TriviaQuestionsResponse,
        activityId: String
    ) = data.results.map { result ->
        Question(
            id = UUID.randomUUID().toString(),
            activityId = activityId,
            correctAnswer = result.correctAnswer.translated(),
            difficulty = result.difficulty,
            answers = result.getAnswers(),
            question = result.question.translated(),
            type = result.type
        )
    }

    private suspend fun TriviaQuestionResult.getAnswers() =
        (incorrectAnswers + correctAnswer).shuffled().map { answer ->
            answer.translated()
        }

    private suspend fun String.translated() = translatorService.translate(this)
}