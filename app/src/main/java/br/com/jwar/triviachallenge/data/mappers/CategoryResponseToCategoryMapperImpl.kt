package br.com.jwar.triviachallenge.data.mappers

import br.com.jwar.triviachallenge.domain.model.Category
import br.com.jwar.triviachallenge.data.services.responses.CategoryResponse

class CategoryResponseToCategoryMapperImpl : CategoryResponseToCategoryMapper {
    override fun mapFrom(categoryResponse: CategoryResponse): List<Category> {
        return categoryResponse.map { entry ->
            Category(
                id = entry.key,
                name = entry.value
            )
        }
    }
}