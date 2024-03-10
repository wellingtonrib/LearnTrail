package br.com.jwar.triviachallenge.data.datasources

import br.com.jwar.triviachallenge.data.services.responses.CategoryLessonResponse
import br.com.jwar.triviachallenge.data.services.responses.CategoryResponse

class CategoryDataSourceImpl: CategoryDataSource {

    private val categories = listOf(
        CategoryResponse("9", "General Knowledge"),
        CategoryResponse("10", "Entertainment: Books"),
        CategoryResponse("11", "Entertainment: Film"),
        CategoryResponse("12", "Entertainment: Music"),
        CategoryResponse("13", "Entertainment: Musicals & Theatres"),
        CategoryResponse("14", "Entertainment: Television"),
        CategoryResponse("15", "Entertainment: Video Games"),
        CategoryResponse("16", "Entertainment: Board Games"),
        CategoryResponse("17", "Science & Nature"),
        CategoryResponse("18", "Science: Computers"),
        CategoryResponse("19", "Science: Mathematics"),
        CategoryResponse("20", "Mythology"),
        CategoryResponse("21", "Sports"),
        CategoryResponse("22", "Geography"),
        CategoryResponse("23", "History"),
        CategoryResponse("24", "Politics"),
        CategoryResponse("25", "Art"),
        CategoryResponse("26", "Celebrities"),
        CategoryResponse("27", "Animals"),
        CategoryResponse("28", "Vehicles"),
        CategoryResponse("29", "Entertainment: Comics"),
        CategoryResponse("30", "Science: Gadgets"),
        CategoryResponse("31", "Entertainment: Japanese Anime & Manga"),
        CategoryResponse("32", "Entertainment: Cartoon & Animations"),
    )

    override suspend fun getCategories() =
        categories.map { categoryResponse ->
            categoryResponse.copy(
                lessons = listOf(
                    CategoryLessonResponse("easy", "Easy"),
                    CategoryLessonResponse("medium", "Medium"),
                    CategoryLessonResponse("hard", "Hard"),
                )
            )
        }
}