package br.com.jwar.triviachallenge.domain.repositories

import br.com.jwar.triviachallenge.domain.model.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun getCategories(): Flow<List<Category>>
}