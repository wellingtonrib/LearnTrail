package br.com.jwar.triviachallenge.data.mappers

import br.com.jwar.triviachallenge.domain.model.Unit
import br.com.jwar.triviachallenge.data.datasources.trivia.dto.TriviaCategoryResponse
import br.com.jwar.triviachallenge.data.services.translator.TranslatorService
import br.com.jwar.triviachallenge.domain.model.Lesson
import javax.inject.Inject

class TriviaCategoryResponseToUnitMapper @Inject constructor(
    private val translatorService: TranslatorService,
) {
    suspend fun mapFrom(triviaCategoryResponse: List<TriviaCategoryResponse>) =
        triviaCategoryResponse.map { response ->
            Unit(
                id = response.id,
                name = translatorService.translate(response.name),
                lessons = getLessons()
            )
        }

    private suspend fun getLessons() = listOf(
        Lesson(id = "easy", name = translatorService.translate("Easy")),
        Lesson(id = "medium", name = translatorService.translate("Medium")),
        Lesson(id = "hard", name = translatorService.translate("Difficult"))
    )
}