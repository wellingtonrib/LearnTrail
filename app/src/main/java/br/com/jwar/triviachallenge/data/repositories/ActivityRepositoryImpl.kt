package br.com.jwar.triviachallenge.data.repositories

import br.com.jwar.triviachallenge.data.datasources.local.LocalDataSourceStrategy
import br.com.jwar.triviachallenge.data.datasources.remote.RemoteDataSourceStrategy
import br.com.jwar.triviachallenge.domain.model.Activity
import br.com.jwar.triviachallenge.domain.repositories.ActivityRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.first

class ActivityRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSourceStrategy,
    private val localDataSource: LocalDataSourceStrategy,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ActivityRepository {
    override fun getActivity(lessonId: String) = flow {
        val localActivity = localDataSource.getActivity(lessonId).first()
        if (localActivity.questions.isEmpty()) {
            val remoteActivity = runCatching {
                remoteDataSource.getActivity(lessonId).also {
                    localDataSource.saveActivity(it, lessonId)
                }
            }
            emitOrFail(remoteActivity.getOrDefault(localActivity))
        } else {
            emitOrFail(localActivity)
        }
    }
    .flowOn(dispatcher)

    private suspend fun FlowCollector<Activity>.emitOrFail(activity: Activity) {
        if (activity.questions.isEmpty()) {
            throw IllegalArgumentException("Activity has no questions")
        } else {
            emit(activity)
        }
    }
}
