package br.com.jwar.learntrail.domain.model

data class Unit(
    val id: String,
    val name: String,
    val isUnlocked: Boolean = false,
)

