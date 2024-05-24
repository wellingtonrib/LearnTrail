package br.com.jwar.triviachallenge.domain.model

data class Activity(
    val id: String,
    val name: String,
    val unitId: String,
    val isUnlocked: Boolean = false,
    val isCompleted: Boolean = false,
)

