package br.com.jwar.triviachallenge.data.repositories

import br.com.jwar.triviachallenge.data.datasources.local.LocalDataSource
import br.com.jwar.triviachallenge.data.datasources.remote.RemoteDataSource
import br.com.jwar.triviachallenge.domain.repositories.UnitRepository
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class UnitRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : UnitRepository {
    override fun getUnits(refresh: Boolean) = flow {
        val localUnits = localDataSource.getUnits()
        if (refresh || localUnits.first().isEmpty()) {
            runCatching {
                remoteDataSource.getUnits().also { remoteUnits ->
                    localDataSource.saveUnits(remoteUnits)
                }
            }.onFailure {
                it.printStackTrace()
            }
        }
        emitAll(localUnits)
    }.flowOn(dispatcher)

    override suspend fun unlockUnit(unitId: String) {
        localDataSource.getUnit(unitId).first().let { unit ->
            localDataSource.updateUnit(unit.copy(isUnlocked = true))
        }
    }
}