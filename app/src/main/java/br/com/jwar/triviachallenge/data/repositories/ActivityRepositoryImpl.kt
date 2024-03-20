package br.com.jwar.triviachallenge.data.repositories

import br.com.jwar.triviachallenge.data.datasources.remote.RemoteDataSourceStrategy
import br.com.jwar.triviachallenge.domain.repositories.ActivityRepository
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class ActivityRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSourceStrategy,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ActivityRepository {

    override fun getActivity(unitId: String, activityId: String) = flow {
        val activity = remoteDataSource.getActivity(unitId, activityId)
        emit(activity)
    }.flowOn(dispatcher)
}