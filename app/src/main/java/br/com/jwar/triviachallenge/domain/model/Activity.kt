package br.com.jwar.triviachallenge.domain.model

data class Activity(
    val id: String,
    val name: String,
    val unitId: String,
    val questions: List<Question> = emptyList(),
)

