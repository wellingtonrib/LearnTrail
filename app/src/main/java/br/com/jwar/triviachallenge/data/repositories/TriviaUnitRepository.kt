package br.com.jwar.triviachallenge.data.repositories

import br.com.jwar.triviachallenge.data.datasources.trivia.TriviaRemoteDataSource
import br.com.jwar.triviachallenge.data.mappers.TriviaCategoryResponseToUnitMapper
import br.com.jwar.triviachallenge.domain.repositories.UnitRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

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