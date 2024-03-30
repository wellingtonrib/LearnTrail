package br.com.jwar.triviachallenge.domain.model

data class Activity(
    val lessonId: String,
    val questions: List<Question>,
)

