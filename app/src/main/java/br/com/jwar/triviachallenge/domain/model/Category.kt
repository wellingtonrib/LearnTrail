package br.com.jwar.triviachallenge.domain.model

data class Category(
    val id: String,
    val name: String,
    val lessons: List<CategoryLesson>,
)

data class CategoryLesson(
    val id: String,
    val name: String,
)
