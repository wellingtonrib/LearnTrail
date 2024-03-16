package br.com.jwar.triviachallenge.data.repositories

import br.com.jwar.triviachallenge.data.datasources.trivia.TriviaRemoteDataSource
import br.com.jwar.triviachallenge.data.mappers.ActivityDataToDomainMapper
import br.com.jwar.triviachallenge.domain.repositories.ActivityRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ActivityRepositoryImpl @Inject constructor(
    private val triviaRemoteDataSource: TriviaRemoteDataSource,
    private val activityDataToDomainMapper: ActivityDataToDomainMapper,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ActivityRepository {

    override fun getActivity(unitId: String, activityId: String) = flow {
        val response = triviaRemoteDataSource.getQuestion(unitId, activityId)
        val activity = activityDataToDomainMapper.mapFrom(response)
        emit(activity)
    }.flowOn(dispatcher)
}