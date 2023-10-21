package br.com.jwar.triviachallenge.data.datasources

import br.com.jwar.triviachallenge.data.services.ChallengeService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ChallengeDataSourceImpl @Inject constructor(
    private val challengeService: ChallengeService,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ChallengeDataSource {

    override suspend fun getChallenge(categoryId: String) = withContext(dispatcher) {
        challengeService.getChallenge(categoryId)
    }
}        