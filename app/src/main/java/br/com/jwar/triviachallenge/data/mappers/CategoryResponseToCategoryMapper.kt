package br.com.jwar.triviachallenge.data.mappers

import br.com.jwar.triviachallenge.domain.model.Category
import br.com.jwar.triviachallenge.data.services.responses.CategoryResponse

interface CategoryResponseToCategoryMapper {
    fun mapFrom(categoryResponse: CategoryResponse): List<Category>
}