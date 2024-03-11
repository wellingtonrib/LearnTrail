package br.com.jwar.triviachallenge.data.mappers

import br.com.jwar.triviachallenge.domain.model.Unit
import br.com.jwar.triviachallenge.data.services.responses.UnitResponse

interface UnitDataToDomainMapper {
    suspend fun mapFrom(unitResponse: List<UnitResponse>): List<Unit>
}