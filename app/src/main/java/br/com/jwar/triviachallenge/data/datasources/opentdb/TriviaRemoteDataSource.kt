package br.com.jwar.triviachallenge.data.datasources.opentdb

import br.com.jwar.triviachallenge.data.datasources.RemoteDataSourceStrategy
import br.com.jwar.triviachallenge.data.datasources.opentdb.dto.TriviaCategoryResponse
import br.com.jwar.triviachallenge.data.datasources.opentdb.dto.TriviaQuestionsResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TriviaRemoteDataSource @Inject constructor(
    private val triviaApi: TriviaApi,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : RemoteDataSourceStrategy<TriviaCategoryResponse, TriviaQuestionsResponse> {

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
    )

    override suspend fun getActivity(
        unitId: String,
        activityId: String
    ) = withContext(dispatcher) {
        triviaApi.getQuestions(unitId, activityId)
    }
}        