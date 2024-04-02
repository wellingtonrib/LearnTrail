package br.com.jwar.triviachallenge.presentation.ui.screens.home

import br.com.jwar.triviachallenge.domain.model.Unit
import br.com.jwar.triviachallenge.domain.repositories.ActivityRepository
import br.com.jwar.triviachallenge.domain.repositories.UnitRepository
import br.com.jwar.triviachallenge.presentation.model.ActivityModel
import br.com.jwar.triviachallenge.presentation.model.UnitModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

interface HomeViewProcessor {

    val activityRepository: ActivityRepository
    val unitRepository: UnitRepository

    suspend fun List<Unit>.toUnitModels() = combine(
        this.map { unit ->
            activityRepository.getActivities(unit.id).map { activities ->
                val activityModels = activities.map { ActivityModel.fromActivity(it) }
                UnitModel.fromUnit(unit = unit, activities = activityModels)
            }.distinctUntilChanged()
        }
    ) { unitModels -> unitModels.toList() }

    suspend fun unlockUnitsOrActivitiesIfNeeded(units: List<UnitModel>) {
        if (units.isAllLocked()) {
            unlockFirstUnit(units)
        } else {
            val lastUnlockedUnit = units.getLastUnlocked() ?: return
            if (lastUnlockedUnit.hasAllActivitiesCompleted()) {
                unlockNextUnit(units)
            } else {
                unlockNextActivity(lastUnlockedUnit)
            }
        }
    }

    suspend fun unlockFirstUnit(units: List<UnitModel>) {
        units.firstOrNull()?.let { firstUnit ->
            unitRepository.unlockUnit(firstUnit.id)
            unlockFirstActivity(firstUnit)
        }
    }

    suspend fun unlockNextUnit(units: List<UnitModel>) {
        val lastUnlockedUnit = units.lastOrNull { it.isUnlocked }
        val nextUnit = units.zipWithNext().find { it.first == lastUnlockedUnit }?.second
        if (nextUnit != null && nextUnit.isUnlocked.not()) {
            unitRepository.unlockUnit(nextUnit.id)
            unlockFirstActivity(nextUnit)
        }
    }

    suspend fun unlockFirstActivity(unit: UnitModel) =
        unit.activities.firstOrNull()?.let { activityRepository.unlockActivity(it.id) }

    suspend fun unlockNextActivity(unit: UnitModel) {
        val lastUnlockedCompletedActivity =
            unit.activities.lastOrNull { it.isUnlocked && it.isCompleted }
        val nextActivity = unit.activities.zipWithNext().find { it.first == lastUnlockedCompletedActivity }?.second
        if (nextActivity != null && nextActivity.isUnlocked.not()) {
            activityRepository.unlockActivity(nextActivity.id)
        }
    }

    fun UnitModel.hasAllActivitiesCompleted() = this.activities.all { it.isCompleted }

    fun List<UnitModel>.isAllLocked() = this.all { !it.isUnlocked }

    fun List<UnitModel>.getLastUnlocked() = this.lastOrNull { it.isUnlocked }
}