package br.com.jwar.triviachallenge.data.repositories

import br.com.jwar.triviachallenge.data.datasources.UnitsDataSource
import br.com.jwar.triviachallenge.data.mappers.UnitDataToDomainMapper
import br.com.jwar.triviachallenge.domain.repositories.UnitRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class UnitRepositoryImpl @Inject constructor(
    private val unitsDataSource: UnitsDataSource,
    private val unitDataToDomainMapper: UnitDataToDomainMapper,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : UnitRepository {
    override fun getUnits() = flow {
        val response = unitsDataSource.getUnits()
        val units = unitDataToDomainMapper.mapFrom(response)
        emit(units)
    }.flowOn(dispatcher)
}