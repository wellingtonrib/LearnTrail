package br.com.jwar.triviachallenge.data.datasources.remote

import br.com.jwar.triviachallenge.domain.model.Activity
import br.com.jwar.triviachallenge.domain.model.Question
import br.com.jwar.triviachallenge.domain.model.Unit

interface RemoteDataSource {
    suspend fun getUnits(): List<Unit>
    suspend fun getActivities(unitId: String): List<Activity>
    suspend fun getQuestions(activityId: String): List<Question>
}