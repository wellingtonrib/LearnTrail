package br.com.jwar.triviachallenge.data.services.responses

data class UnitResponse(
    val id: String,
    val name: String,
    val lessons: List<LessonResponse> = emptyList(),
)

