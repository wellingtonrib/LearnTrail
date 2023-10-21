package br.com.jwar.triviachallenge.data.repositories

import br.com.jwar.triviachallenge.data.datasources.ChallengeDataSource
import br.com.jwar.triviachallenge.data.mappers.ChallengeResponseToChallengeMapper
import br.com.jwar.triviachallenge.domain.repositories.ChallengeRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ChallengeRepositoryImpl @Inject constructor(
    private val challengeDataSource: ChallengeDataSource,
    private val challengeResponseToChallengeMapper: ChallengeResponseToChallengeMapper,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ChallengeRepository {

    override fun getChallenge(categoryId: String) = flow {
        val challengeResponse = challengeDataSource.getChallenge(categoryId)
        val challenge = challengeResponseToChallengeMapper.mapFrom(challengeResponse)
        emit(challenge)
    }.flowOn(dispatcher)
}