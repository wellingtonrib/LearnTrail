package br.com.jwar.triviachallenge.domain.model

data class Question(
    val category: String,
    val correctAnswer: String,
    val difficulty: String,
    val answers: List<String>,
    val question: String,
    val type: String
)