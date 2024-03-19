package br.com.jwar.triviachallenge.data.datasources

interface RemoteDataSourceStrategy<U, A> {
    suspend fun getUnits(): List<U>
    suspend fun getActivity(unitId: String, activityId: String): A
}