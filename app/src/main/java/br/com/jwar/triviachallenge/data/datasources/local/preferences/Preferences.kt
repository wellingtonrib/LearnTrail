package br.com.jwar.triviachallenge.data.datasources.local.preferences

import kotlinx.coroutines.flow.Flow

interface Preferences {
    suspend fun addXp(xp: Int)
    fun getXp(): Flow<Int>
}