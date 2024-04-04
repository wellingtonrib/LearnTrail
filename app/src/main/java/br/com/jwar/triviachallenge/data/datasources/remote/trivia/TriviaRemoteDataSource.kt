package br.com.jwar.triviachallenge.data.datasources.remote.trivia

import br.com.jwar.triviachallenge.data.datasources.remote.RemoteDataSourceStrategy
import br.com.jwar.triviachallenge.data.datasources.remote.trivia.dto.TriviaCategoryResponse
import br.com.jwar.triviachallenge.domain.model.Activity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TriviaRemoteDataSource @Inject constructor(
    private val triviaApi: TriviaApi,
    private val triviaAdapter: TriviaRemoteDataSourceAdapter,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : RemoteDataSourceStrategy {

    override suspend fun getUnits() = listOf(
        TriviaCategoryResponse("9", "General Knowledge"),
        TriviaCategoryResponse("10", "Entertainment: Books"),
        TriviaCategoryResponse("11", "Entertainment: Film"),
        TriviaCategoryResponse("12", "Entertainment: Music"),
        TriviaCategoryResponse("13", "Entertainment: Musicals & Theatres"),
        TriviaCategoryResponse("14", "Entertainment: Television"),
        TriviaCategoryResponse("15", "Entertainment: Video Games"),
        TriviaCategoryResponse("16", "Entertainment: Board Games"),
        TriviaCategoryResponse("17", "Science & Nature"),
        TriviaCategoryResponse("18", "Science: Computers"),
        TriviaCategoryResponse("19", "Science: Mathematics"),
        TriviaCategoryResponse("20", "Mythology"),
        TriviaCategoryResponse("21", "Sports"),
        TriviaCategoryResponse("22", "Geography"),
        TriviaCategoryResponse("23", "History"),
        TriviaCategoryResponse("24", "Politics"),
        TriviaCategoryResponse("25", "Art"),
        TriviaCategoryResponse("26", "Celebrities"),
        TriviaCategoryResponse("27", "Animals"),
        TriviaCategoryResponse("28", "Vehicles"),
        TriviaCategoryResponse("29", "Entertainment: Comics"),
        TriviaCategoryResponse("30", "Science: Gadgets"),
        TriviaCategoryResponse("31", "Entertainment: Japanese Anime & Manga"),
        TriviaCategoryResponse("32", "Entertainment: Cartoon & Animations"),
    ).map { triviaAdapter.adaptToUnit(it) }

    override suspend fun getActivities(unitId: String) = listOf(
        Activity(id = "$unitId:easy", name = "Easy", unitId = unitId),
        Activity(id = "$unitId:medium", name = "Medium", unitId = unitId),
        Activity(id = "$unitId:hard", name = "Difficult", unitId = unitId)
    ).map { triviaAdapter.adaptToActivity(it) }

    override suspend fun getQuestions(
        activityId: String
    ) = withContext(dispatcher) {
        val (categoryId, difficult) = activityId.split(":")
        triviaApi.getQuestions(categoryId, difficult).let { response ->
            triviaAdapter.adaptToQuestions(response, activityId)
        }
    }
}        