package br.com.jwar.triviachallenge.data.repositories

import br.com.jwar.triviachallenge.data.datasources.local.LocalDataSourceStrategy
import br.com.jwar.triviachallenge.data.datasources.remote.RemoteDataSourceStrategy
import br.com.jwar.triviachallenge.domain.repositories.UnitRepository
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart

class UnitRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSourceStrategy,
    private val localDataSource: LocalDataSourceStrategy,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : UnitRepository {
    override fun getUnits() = flow {
        val localUnits = localDataSource.getUnits()
        emitAll(localUnits)
    }.onStart {
        val remoteUnits = remoteDataSource.getUnits()
        localDataSource.saveUnits(remoteUnits)
    }.catch { exception ->
        exception.printStackTrace()
    }
    .flowOn(dispatcher)
}