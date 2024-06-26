package br.com.jwar.learntrail.data.repositories

import br.com.jwar.learntrail.data.datasources.local.database.LocalDataSource
import br.com.jwar.learntrail.data.datasources.remote.RemoteDataSource
import br.com.jwar.learntrail.domain.repositories.ActivityRepository
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class ActivityRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ActivityRepository {
    override suspend fun getActivities(unitId: String, refresh: Boolean) = flow {
        val localActivities = localDataSource.getActivities(unitId)
        if (refresh || localActivities.first().isEmpty()) {
            remoteDataSource.getActivities(unitId).also { remoteActivities ->
                localDataSource.saveActivities(remoteActivities)
            }
        }
        emitAll(localActivities)
    }.flowOn(dispatcher)

    override suspend fun getQuestions(activityId: String) = flow {
        val localQuestions = localDataSource.getQuestions(activityId)
        remoteDataSource.getQuestions(activityId).also { remoteQuestions ->
            localDataSource.saveQuestions(remoteQuestions)
        }
        emitAll(localQuestions)
    }
    .flowOn(dispatcher)

    override suspend fun completeActivity(activityId: String) =
        localDataSource.getActivity(activityId).first().let { activity ->
            localDataSource.updateActivity(activity.copy(isCompleted = true))
        }

    override suspend fun unlockActivity(activityId: String) =
        localDataSource.getActivity(activityId).first().let { activity ->
            localDataSource.updateActivity(activity.copy(isUnlocked = true))
        }
}
