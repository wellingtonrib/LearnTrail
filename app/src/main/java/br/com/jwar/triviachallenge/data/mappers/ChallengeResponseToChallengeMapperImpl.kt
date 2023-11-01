package br.com.jwar.triviachallenge.data.mappers

import br.com.jwar.triviachallenge.domain.model.Challenge
import br.com.jwar.triviachallenge.domain.model.Question
import br.com.jwar.triviachallenge.data.services.responses.ChallengeResponse
import br.com.jwar.triviachallenge.data.services.responses.Result
import br.com.jwar.triviachallenge.data.services.translator.TranslatorService
import javax.inject.Inject

class ChallengeResponseToChallengeMapperImpl @Inject constructor(
    private val translatorService: TranslatorService
) : ChallengeResponseToChallengeMapper {
    override suspend fun mapFrom(challengeResponse: ChallengeResponse): Challenge {
        return Challenge(
            questions = challengeResponse.results.map { result ->
                Question(
                    category = mapCategory(result),
                    correctAnswer = mapCorrectAnswer(result),
                    difficulty = result.difficulty,
                    answers = mapAnswers(result),
                    question = mapQuestion(result),
                    type = result.type
                )
            }
        )
    }

    private suspend fun mapCategory(result: Result) =
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