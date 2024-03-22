package br.com.jwar.triviachallenge.domain.repositories

import br.com.jwar.triviachallenge.domain.model.Unit
import kotlinx.coroutines.flow.Flow

interface UnitRepository {
    fun getUnits(refresh: Boolean): Flow<List<Unit>>
}