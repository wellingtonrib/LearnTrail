package br.com.jwar.triviachallenge.data.repositories

import br.com.jwar.triviachallenge.data.datasources.local.preferences.UserPreferences
import br.com.jwar.triviachallenge.domain.repositories.UserRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class UserRepositoryImpl @Inject constructor(
    private val userPreferences: UserPreferences,
) : UserRepository {
    override suspend fun getXP(): Flow<Int> = userPreferences.getXp()

    override suspend fun addXP(xp: Int) = userPreferences.addXp(xp)
}