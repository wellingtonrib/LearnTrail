package br.com.jwar.learntrail.data.repositories

import br.com.jwar.learntrail.data.datasources.local.LocalDataSourceStrategy
import br.com.jwar.learntrail.data.datasources.remote.RemoteDataSourceStrategy
import br.com.jwar.learntrail.domain.repositories.ActivityRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
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
            emit(remoteActivity.getOrDefault(localActivity))
        } else {
            emit(localActivity)
        }
    }
    .flowOn(dispatcher)
}