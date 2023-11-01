package br.com.jwar.triviachallenge.data.mappers

import br.com.jwar.triviachallenge.domain.model.Category
import br.com.jwar.triviachallenge.data.services.responses.CategoryResponse
import br.com.jwar.triviachallenge.data.services.translator.TranslatorService
import javax.inject.Inject

class CategoryResponseToCategoryMapperImpl @Inject constructor(
    private val translatorService: TranslatorService
) : CategoryResponseToCategoryMapper {
    override suspend fun mapFrom(categoryResponse: CategoryResponse): List<Category> {
        return categoryResponse.map { entry ->
            Category(
                id = entry.key,
                name = translatorService.translate(entry.value)
            )
        }
    }
}