package br.com.jwar.triviachallenge.domain.repositories

import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getXP(): Flow<Int>
    fun saveXP(xp: Int)
}