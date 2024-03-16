package br.com.jwar.triviachallenge.data.mappers

import br.com.jwar.triviachallenge.domain.model.Activity
import br.com.jwar.triviachallenge.data.services.responses.TriviaQuestionsResponse

interface ActivityDataToDomainMapper {
    suspend fun mapFrom(triviaQuestionsResponse: TriviaQuestionsResponse): Activity
}