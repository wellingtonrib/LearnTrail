package br.com.jwar.triviachallenge.data.repositories

import br.com.jwar.triviachallenge.data.datasources.TriviaRemoteDataSource
import br.com.jwar.triviachallenge.data.mappers.TriviaQuestionResponseToActivityMapper
import br.com.jwar.triviachallenge.domain.repositories.ActivityRepository
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class TriviaActivityRepository @Inject constructor(
    private val triviaRemoteDataSource: TriviaRemoteDataSource,
    private val triviaQuestionResponseToActivityMapper: TriviaQuestionResponseToActivityMapper,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ActivityRepository {

    override fun getActivity(unitId: String, activityId: String) = flow {
        val response = triviaRemoteDataSource.getQuestion(unitId, activityId)
        val activity = triviaQuestionResponseToActivityMapper.mapFrom(response)
        emit(activity)
    }.flowOn(dispatcher)
}