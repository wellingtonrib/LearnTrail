package br.com.jwar.triviachallenge.data.datasources

import br.com.jwar.triviachallenge.data.services.responses.ChallengeResponse

interface ChallengeDataSource {
    suspend fun getChallenge(categoryId: String): ChallengeResponse
}