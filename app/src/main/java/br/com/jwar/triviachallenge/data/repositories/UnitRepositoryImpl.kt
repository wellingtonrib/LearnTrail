package br.com.jwar.triviachallenge.data.repositories

import br.com.jwar.triviachallenge.data.datasources.local.LocalDataSourceStrategy
import br.com.jwar.triviachallenge.data.datasources.remote.RemoteDataSourceStrategy
import br.com.jwar.triviachallenge.domain.repositories.UnitRepository
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class UnitRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSourceStrategy,
    private val localDataSource: LocalDataSourceStrategy,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : UnitRepository {
    override fun getUnits(refresh: Boolean) = flow {
        val localUnits = localDataSource.getUnits().first()
        if (refresh || localUnits.isEmpty()) {
            val remoteUnits = runCatching {
                remoteDataSource.getUnits().also {
                    localDataSource.saveUnits(it)
                }
            }
            emit(remoteUnits.getOrDefault(localUnits))
        } else {
            emit(localUnits)
        }
    }.flowOn(dispatcher)
}