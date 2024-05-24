package br.com.jwar.learntrail.domain.repositories

import br.com.jwar.learntrail.domain.model.Activity
import kotlinx.coroutines.flow.Flow

interface ActivityRepository {
    fun getActivity(lessonId: String): Flow<Activity>
}