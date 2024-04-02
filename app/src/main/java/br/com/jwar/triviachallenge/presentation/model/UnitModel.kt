package br.com.jwar.triviachallenge.presentation.model

import br.com.jwar.triviachallenge.domain.model.Unit

data class UnitModel(
    val id: String,
    val name: String,
    val activities: List<ActivityModel>,
    val isUnlocked: Boolean = false,
) {
    val isCompleted: Boolean
        get() = activities.all { it.isCompleted }

    companion object {
        fun fromUnit(unit: Unit, activities: List<ActivityModel>) = UnitModel(
            id = unit.id,
            name = unit.name,
            activities = activities,
            isUnlocked = unit.isUnlocked,
        )
    }
}