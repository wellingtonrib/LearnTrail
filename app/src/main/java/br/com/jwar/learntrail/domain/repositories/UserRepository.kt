package br.com.jwar.learntrail.domain.repositories

import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getXP(): Flow<Int>
    suspend fun addXP(xp: Int)
}