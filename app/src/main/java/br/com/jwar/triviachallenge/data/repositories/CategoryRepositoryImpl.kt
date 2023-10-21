package br.com.jwar.triviachallenge.data.repositories

import br.com.jwar.triviachallenge.data.datasources.CategoryDataSource
import br.com.jwar.triviachallenge.data.mappers.CategoryResponseToCategoryMapper
import br.com.jwar.triviachallenge.domain.repositories.CategoryRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val categoryDataSource: CategoryDataSource,
    private val categoryResponseToCategoryMapper: CategoryResponseToCategoryMapper,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : CategoryRepository {
    override fun getCategories() = flow {
        val categoriesResponse = categoryDataSource.getCategories()
        val categories = categoryResponseToCategoryMapper.mapFrom(categoriesResponse)
        emit(categories)
    }.flowOn(dispatcher)
}