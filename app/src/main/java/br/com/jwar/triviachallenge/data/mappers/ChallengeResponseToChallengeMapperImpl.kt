package br.com.jwar.triviachallenge.data.mappers

import br.com.jwar.triviachallenge.domain.model.Challenge
import br.com.jwar.triviachallenge.domain.model.Question
import br.com.jwar.triviachallenge.data.services.responses.ChallengeResponse

class ChallengeResponseToChallengeMapperImpl : ChallengeResponseToChallengeMapper {
    override fun mapFrom(challengeResponse: ChallengeResponse): Challenge {
        return Challenge(
            questions = challengeResponse.results.map {
                Question(
                    category = it.category,
                    correctAnswer = it.correctAnswer,
                    difficulty = it.difficulty,
                    answers = (it.incorrectAnswers + it.correctAnswer).shuffled(),
                    question = it.question,
                    type = it.type
                )
            }
        )
    }
}