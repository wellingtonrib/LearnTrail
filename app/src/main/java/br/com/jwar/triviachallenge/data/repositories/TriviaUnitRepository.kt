package br.com.jwar.triviachallenge.data.repositories

import br.com.jwar.triviachallenge.data.datasources.TriviaRemoteDataSource
import br.com.jwar.triviachallenge.data.mappers.TriviaCategoryResponseToUnitMapper
import br.com.jwar.triviachallenge.domain.repositories.UnitRepository
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class TriviaUnitRepository @Inject constructor(
    private val triviaRemoteDataSource: TriviaRemoteDataSource,
    private val triviaCategoryToUnitMapper: TriviaCategoryResponseToUnitMapper,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : UnitRepository {
    override fun getUnits() = flow {
        val response = triviaRemoteDataSource.getCategories()
        val units = triviaCategoryToUnitMapper.mapFrom(response)
        emit(units)
    }.flowOn(dispatcher)
}