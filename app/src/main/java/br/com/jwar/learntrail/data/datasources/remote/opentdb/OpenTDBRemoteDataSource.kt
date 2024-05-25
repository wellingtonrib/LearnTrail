package br.com.jwar.learntrail.data.datasources.remote.opentdb

import br.com.jwar.learntrail.data.datasources.remote.RemoteDataSource
import br.com.jwar.learntrail.data.datasources.remote.opentdb.dto.OpenTDBCategoryResponse
import br.com.jwar.learntrail.domain.model.Activity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class OpenTDBRemoteDataSource @Inject constructor(
    private val openTDBApi: OpenTDBApi,
    private val openTDBAdapter: OpenTDBRemoteDataSourceAdapter,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : RemoteDataSource {

    override suspend fun getUnits() = listOf(
        OpenTDBCategoryResponse("9", "General Knowledge"),
        OpenTDBCategoryResponse("10", "Entertainment: Books"),
        OpenTDBCategoryResponse("11", "Entertainment: Film"),
        OpenTDBCategoryResponse("12", "Entertainment: Music"),
        OpenTDBCategoryResponse("13", "Entertainment: Musicals & Theatres"),
        OpenTDBCategoryResponse("14", "Entertainment: Television"),
        OpenTDBCategoryResponse("15", "Entertainment: Video Games"),
        OpenTDBCategoryResponse("16", "Entertainment: Board Games"),
        OpenTDBCategoryResponse("17", "Science & Nature"),
        OpenTDBCategoryResponse("18", "Science: Computers"),
        OpenTDBCategoryResponse("19", "Science: Mathematics"),
        OpenTDBCategoryResponse("20", "Mythology"),
        OpenTDBCategoryResponse("21", "Sports"),
        OpenTDBCategoryResponse("22", "Geography"),
        OpenTDBCategoryResponse("23", "History"),
        OpenTDBCategoryResponse("24", "Politics"),
        OpenTDBCategoryResponse("25", "Art"),
        OpenTDBCategoryResponse("26", "Celebrities"),
        OpenTDBCategoryResponse("27", "Animals"),
        OpenTDBCategoryResponse("28", "Vehicles"),
        OpenTDBCategoryResponse("29", "Entertainment: Comics"),
        OpenTDBCategoryResponse("30", "Science: Gadgets"),
        OpenTDBCategoryResponse("31", "Entertainment: Japanese Anime & Manga"),
        OpenTDBCategoryResponse("32", "Entertainment: Cartoon & Animations"),
    ).map { openTDBAdapter.adaptToUnit(it) }

    override suspend fun getActivities(unitId: String) = listOf(
        Activity(id = "$unitId:easy", name = "Easy", unitId = unitId),
        Activity(id = "$unitId:medium", name = "Medium", unitId = unitId),
        Activity(id = "$unitId:hard", name = "Difficult", unitId = unitId)
    ).map { openTDBAdapter.adaptToActivity(it) }

    override suspend fun getQuestions(
        activityId: String
    ) = withContext(dispatcher) {
        val (categoryId, difficult) = activityId.split(":")
        openTDBApi.getQuestions(categoryId, difficult).let { response ->
            openTDBAdapter.adaptToQuestions(response, activityId)
        }
    }
}        