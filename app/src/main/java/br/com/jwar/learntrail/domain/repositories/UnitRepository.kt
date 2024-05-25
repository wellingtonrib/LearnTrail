package br.com.jwar.learntrail.domain.repositories

import br.com.jwar.learntrail.domain.model.Unit
import kotlinx.coroutines.flow.Flow

interface UnitRepository {
    fun getUnits(refresh: Boolean): Flow<List<Unit>>
    suspend fun unlockUnit(unitId: String)
}