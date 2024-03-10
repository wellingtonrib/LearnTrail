package br.com.jwar.triviachallenge.data.mappers

import br.com.jwar.triviachallenge.domain.model.Category
import br.com.jwar.triviachallenge.data.services.responses.CategoryResponse
import br.com.jwar.triviachallenge.data.services.translator.TranslatorService
import br.com.jwar.triviachallenge.domain.model.CategoryLesson
import javax.inject.Inject

class CategoryResponseToCategoryMapperImpl @Inject constructor(
    private val translatorService: TranslatorService,
) : CategoryResponseToCategoryMapper {
    override suspend fun mapFrom(categoryResponse: List<CategoryResponse>): List<Category> {
        return categoryResponse.map { category ->
            Category(
                id = category.id,
                name = translatorService.translate(category.name),
                lessons = category.lessons.map { lesson ->
                    CategoryLesson(
                        id = lesson.id,
                        name = translatorService.translate(lesson.name),
                    )
                }
            )
        }
    }
}