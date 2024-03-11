package br.com.jwar.triviachallenge.data.datasources

import br.com.jwar.triviachallenge.data.services.ActivityService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ActivityDataSourceImpl @Inject constructor(
    private val activityService: ActivityService,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ActivityDataSource {

    override suspend fun getActivity(
        unitId: String,
        activityId: String,
    ) = withContext(dispatcher) {
        activityService.getActivity(unitId, activityId)
    }
}        