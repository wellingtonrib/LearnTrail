package br.com.jwar.triviachallenge.domain.model

data class Unit(
    val id: String,
    val name: String,
    val isUnlocked: Boolean = false,
)

