package br.com.jwar.triviachallenge.data.datasources.local

import br.com.jwar.triviachallenge.domain.model.Unit
import kotlinx.coroutines.flow.Flow

interface LocalDataSourceStrategy {
    suspend fun getUnits(): Flow<List<Unit>>
    suspend fun saveUnits(units: List<Unit>)
}