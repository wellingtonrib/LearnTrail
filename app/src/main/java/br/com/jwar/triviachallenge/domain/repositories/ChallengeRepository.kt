package br.com.jwar.triviachallenge.domain.repositories

import br.com.jwar.triviachallenge.domain.model.Challenge
import kotlinx.coroutines.flow.Flow

interface ChallengeRepository {
    fun getChallenge(categoryId: String): Flow<Challenge>
}