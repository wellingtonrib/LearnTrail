package br.com.jwar.triviachallenge.data.repositories

import br.com.jwar.triviachallenge.data.datasources.local.LocalDataSourceStrategy
import br.com.jwar.triviachallenge.data.datasources.remote.RemoteDataSourceStrategy
import br.com.jwar.triviachallenge.domain.repositories.ActivityRepository
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class ActivityRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSourceStrategy,
    private val localDataSource: LocalDataSourceStrategy,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ActivityRepository {
    override fun getActivity(activityId: String) = flow {
        val localActivity = localDataSource.getActivity(activityId)
        if (localActivity.first().questions.isEmpty()) {
            runCatching {
                remoteDataSource.getActivity(activityId).also { remoteActivity ->
                    localDataSource.saveActivity(remoteActivity, activityId)
                }
            }
        }
        emitAll(localActivity)
    }
    .flowOn(dispatcher)

    override suspend fun completeActivity(activityId: String) {
        localDataSource.completeActivity(activityId)
    }

    override suspend fun unlockActivity(id: String) {
        localDataSource.unlockActivity(id)
    }
}
