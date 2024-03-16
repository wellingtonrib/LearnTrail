package br.com.jwar.triviachallenge.data.mappers

import br.com.jwar.triviachallenge.domain.model.Unit
import br.com.jwar.triviachallenge.data.datasources.trivia.dto.TriviaCategoryResponse

interface UnitDataToDomainMapper {
    suspend fun mapFrom(triviaCategoryResponse: List<TriviaCategoryResponse>): List<Unit>
}