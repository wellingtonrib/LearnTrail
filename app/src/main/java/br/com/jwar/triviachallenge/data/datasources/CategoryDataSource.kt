package br.com.jwar.triviachallenge.data.datasources

import br.com.jwar.triviachallenge.data.services.responses.CategoryResponse

interface CategoryDataSource {
    suspend fun getCategories(): List<CategoryResponse>
}