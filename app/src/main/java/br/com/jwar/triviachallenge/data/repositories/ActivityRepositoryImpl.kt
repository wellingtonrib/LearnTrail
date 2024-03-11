package br.com.jwar.triviachallenge.data.repositories

import br.com.jwar.triviachallenge.data.datasources.ActivityDataSource
import br.com.jwar.triviachallenge.data.mappers.ActivityDataToDomainMapper
import br.com.jwar.triviachallenge.domain.repositories.ActivityRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ActivityRepositoryImpl @Inject constructor(
    private val activityDataSource: ActivityDataSource,
    private val activityDataToDomainMapper: ActivityDataToDomainMapper,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ActivityRepository {

    override fun getActivity(unitId: String, activityId: String) = flow {
        val response = activityDataSource.getActivity(unitId, activityId)
        val activity = activityDataToDomainMapper.mapFrom(response)
        emit(activity)
    }.flowOn(dispatcher)
}