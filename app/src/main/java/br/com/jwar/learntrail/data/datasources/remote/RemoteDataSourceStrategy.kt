package br.com.jwar.learntrail.data.datasources.remote

import br.com.jwar.learntrail.domain.model.Activity
import br.com.jwar.learntrail.domain.model.Unit

interface RemoteDataSourceStrategy {
    suspend fun getUnits(): List<Unit>
    suspend fun getActivity(lessonId: String): Activity
}