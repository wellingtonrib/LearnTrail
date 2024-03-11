package br.com.jwar.triviachallenge.data.datasources

import br.com.jwar.triviachallenge.data.services.responses.LessonResponse
import br.com.jwar.triviachallenge.data.services.responses.UnitResponse

class UnitsDataSourceImpl: UnitsDataSource {

    private val categories = listOf(
        UnitResponse("9", "General Knowledge"),
        UnitResponse("10", "Entertainment: Books"),
        UnitResponse("11", "Entertainment: Film"),
        UnitResponse("12", "Entertainment: Music"),
        UnitResponse("13", "Entertainment: Musicals & Theatres"),
        UnitResponse("14", "Entertainment: Television"),
        UnitResponse("15", "Entertainment: Video Games"),
        UnitResponse("16", "Entertainment: Board Games"),
        UnitResponse("17", "Science & Nature"),
        UnitResponse("18", "Science: Computers"),
        UnitResponse("19", "Science: Mathematics"),
        UnitResponse("20", "Mythology"),
        UnitResponse("21", "Sports"),
        UnitResponse("22", "Geography"),
        UnitResponse("23", "History"),
        UnitResponse("24", "Politics"),
        UnitResponse("25", "Art"),
        UnitResponse("26", "Celebrities"),
        UnitResponse("27", "Animals"),
        UnitResponse("28", "Vehicles"),
        UnitResponse("29", "Entertainment: Comics"),
        UnitResponse("30", "Science: Gadgets"),
        UnitResponse("31", "Entertainment: Japanese Anime & Manga"),
        UnitResponse("32", "Entertainment: Cartoon & Animations"),
    )

    override suspend fun getUnits() =
        categories.map { categoryResponse ->
            categoryResponse.copy(
                lessons = listOf(
                    LessonResponse("easy", "Easy"),
                    LessonResponse("medium", "Medium"),
                    LessonResponse("hard", "Hard"),
                )
            )
        }
}