package br.com.jwar.triviachallenge.data.datasources

import br.com.jwar.triviachallenge.data.services.responses.UnitResponse

interface UnitsDataSource {
    suspend fun getUnits(): List<UnitResponse>
}