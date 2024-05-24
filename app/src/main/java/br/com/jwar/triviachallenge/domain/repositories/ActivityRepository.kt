package br.com.jwar.triviachallenge.domain.repositories

import br.com.jwar.triviachallenge.domain.model.Activity
import kotlinx.coroutines.flow.Flow

interface ActivityRepository {
    fun getActivity(lessonId: String): Flow<Activity>
}