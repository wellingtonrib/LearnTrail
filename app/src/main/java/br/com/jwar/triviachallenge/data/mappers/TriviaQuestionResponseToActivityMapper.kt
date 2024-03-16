package br.com.jwar.triviachallenge.data.mappers

import br.com.jwar.triviachallenge.data.datasources.dto.TriviaQuestionResult
import br.com.jwar.triviachallenge.data.datasources.dto.TriviaQuestionsResponse
import br.com.jwar.triviachallenge.data.services.translator.TranslatorService
import br.com.jwar.triviachallenge.domain.model.Activity
import br.com.jwar.triviachallenge.domain.model.Question
import javax.inject.Inject

class TriviaQuestionResponseToActivityMapper @Inject constructor(
    private val translatorService: TranslatorService
) {
    suspend fun mapFrom(triviaQuestionsResponse: TriviaQuestionsResponse) =
        Activity(
            questions = triviaQuestionsResponse.results.map { result ->
                Question(
                    unit = result.toUnit(),
                    correctAnswer = result.toCorrectAnswer(),
                    difficulty = result.difficulty,
                    answers = result.toAnswers(),
                    question = result.toQuestion(),
                    type = result.type
                )
            }
        )

    private suspend fun TriviaQuestionResult.toUnit() =
        translatorService.translate(category)

    private suspend fun TriviaQuestionResult.toCorrectAnswer() =
        translatorService.translate(correctAnswer)

    private suspend fun TriviaQuestionResult.toQuestion() =
        translatorService.translate(question)

    private suspend fun TriviaQuestionResult.toAnswers() =
        (incorrectAnswers + correctAnswer).shuffled().map { answer ->
            translatorService.translate(answer)
        }
}