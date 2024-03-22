package br.com.jwar.triviachallenge.data.repositories

import br.com.jwar.triviachallenge.data.datasources.local.LocalDataSourceStrategy
import br.com.jwar.triviachallenge.data.datasources.remote.RemoteDataSourceStrategy
import br.com.jwar.triviachallenge.domain.repositories.ActivityRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ActivityRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSourceStrategy,
    private val localDataSource: LocalDataSourceStrategy,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ActivityRepository {
    override fun getActivity(lessonId: String) = flow {
        localDataSource.getActivity(lessonId).collect { localActivity ->
            if (localActivity.questions.isEmpty()) {
                val remoteActivity = remoteDataSource.getActivity(lessonId)
                localDataSource.saveActivity(remoteActivity, lessonId)
            }
            emit(localActivity)
        }
    }
    .flowOn(dispatcher)
}