package br.com.jwar.triviachallenge.domain.repositories

import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getXP(): Flow<Int>
    suspend fun saveXP(xp: Int)
}