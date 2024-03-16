package br.com.jwar.triviachallenge.data.mappers

import br.com.jwar.triviachallenge.domain.model.Activity
import br.com.jwar.triviachallenge.domain.model.Question
import br.com.jwar.triviachallenge.data.services.responses.TriviaQuestionsResponse
import br.com.jwar.triviachallenge.data.services.responses.Result
import br.com.jwar.triviachallenge.data.services.translator.TranslatorService
import javax.inject.Inject

class ActivityDataToDomainMapperImpl @Inject constructor(
    private val translatorService: TranslatorService
) : ActivityDataToDomainMapper {
    override suspend fun mapFrom(triviaQuestionsResponse: TriviaQuestionsResponse): Activity {
        return Activity(
            questions = triviaQuestionsResponse.results.map { result ->
                Question(
                    unit = mapUnit(result),
                    correctAnswer = mapCorrectAnswer(result),
                    difficulty = result.difficulty,
                    answers = mapAnswers(result),
                    question = mapQuestion(result),
                    type = result.type
                )
            }
        )
    }

    private suspend fun mapUnit(result: Result) =
        translatorService.translate(result.category)

    private suspend fun mapCorrectAnswer(result: Result) =
        translatorService.translate(result.correctAnswer)

    private suspend fun mapQuestion(result: Result) =
        translatorService.translate(result.question)

    private suspend fun mapAnswers(result: Result) =
        (result.incorrectAnswers + result.correctAnswer).shuffled().map { answer ->
            translatorService.translate(answer)
        }
}