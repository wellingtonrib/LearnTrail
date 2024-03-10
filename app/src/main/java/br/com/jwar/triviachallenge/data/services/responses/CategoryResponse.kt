package br.com.jwar.triviachallenge.data.services.responses

data class CategoryResponse(
    val id: String,
    val name: String,
    val lessons: List<CategoryLessonResponse> = emptyList(),
)

data class CategoryLessonResponse(
    val id: String,
    val name: String,
)
