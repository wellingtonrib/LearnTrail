package br.com.jwar.triviachallenge.domain.model

data class Unit(
    val id: String,
    val name: String,
    val activities: List<Activity>,
    val isUnlocked: Boolean = false,
)

