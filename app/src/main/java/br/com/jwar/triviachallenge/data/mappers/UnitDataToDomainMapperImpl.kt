package br.com.jwar.triviachallenge.data.mappers

import br.com.jwar.triviachallenge.domain.model.Unit
import br.com.jwar.triviachallenge.data.datasources.trivia.dto.TriviaCategoryResponse
import br.com.jwar.triviachallenge.data.services.translator.TranslatorService
import br.com.jwar.triviachallenge.domain.model.Lesson
import javax.inject.Inject

class UnitDataToDomainMapperImpl @Inject constructor(
    private val translatorService: TranslatorService,
) : UnitDataToDomainMapper {
    override suspend fun mapFrom(triviaCategoryResponse: List<TriviaCategoryResponse>): List<Unit> {
        return triviaCategoryResponse.map { response ->
            Unit(
                id = response.id,
                name = translatorService.translate(response.name),
                lessons = mapLessons(response)
            )
        }
    }

    private suspend fun mapLessons(response: TriviaCategoryResponse) =
        response.lessons.map { lesson ->
            Lesson(
                id = lesson.id,
                name = translatorService.translate(lesson.name),
            )
        }
}