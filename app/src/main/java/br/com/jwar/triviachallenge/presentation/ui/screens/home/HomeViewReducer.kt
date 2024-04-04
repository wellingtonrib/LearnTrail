package br.com.jwar.triviachallenge.presentation.ui.screens.home

import br.com.jwar.triviachallenge.domain.model.Unit
import br.com.jwar.triviachallenge.domain.repositories.ActivityRepository
import br.com.jwar.triviachallenge.domain.repositories.UnitRepository
import br.com.jwar.triviachallenge.presentation.model.ActivityModel
import br.com.jwar.triviachallenge.presentation.model.UnitModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

interface HomeViewReducer {

    val activityRepository: ActivityRepository
    val unitRepository: UnitRepository

    suspend fun reduce(state: HomeViewState, action: HomeViewState.Action): HomeViewState {
        return when (action) {
            is HomeViewState.Action.OnLoaded -> {
                if (unlockUnitsOrActivitiesIfNeeded(action.units)) {
                    HomeViewState.Loading
                } else {
                    HomeViewState.Loaded(action.units)
                }
            }
        }
    }

    suspend fun List<Unit>.toUnitModels() = combine(
        this.map { unit ->
            activityRepository.getActivities(unit.id).map { activities ->
                val activityModels = activities.map { ActivityModel.fromActivity(it) }
                UnitModel.fromUnit(unit = unit, activities = activityModels)
            }.distinctUntilChanged()
        }
    ) { unitModels -> unitModels.toList() }

    suspend fun unlockUnitsOrActivitiesIfNeeded(units: List<UnitModel>): Boolean {
        if (areAllUnitsLocked(units)) {
            unlockFirstUnit(units)
            return true
        }

        val lastUnlockedUnit = getLastUnlockedUnit(units)
        if (lastUnlockedUnit?.areAllActivitiesCompleted() == true) {
            unlockNextUnit(units)
            return true
        }

        val nextActivityToUnlock = lastUnlockedUnit?.getNextActivityToUnlock()
        if (nextActivityToUnlock != null) {
            activityRepository.unlockActivity(nextActivityToUnlock.id)
            return true
        }

        return false
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

    fun areAllUnitsLocked(units: List<UnitModel>) = units.all { !it.isUnlocked }

    fun getLastUnlockedUnit(units: List<UnitModel>) = units.lastOrNull { it.isUnlocked }

    fun UnitModel.areAllActivitiesCompleted() = this.activities.all { it.isCompleted }

    fun UnitModel.getNextActivityToUnlock() =
        getLastUnlockedCompletedActivity()?.let { lastUnlockedCompletedActivity ->
            getNextLockedActivity(lastUnlockedCompletedActivity)
        }

    fun UnitModel.getNextLockedActivity(activity: ActivityModel) =
        this.activities.zipWithNext().find { (current, next) ->
            current == activity && next.isUnlocked.not()
        }?.second

    fun UnitModel.getLastUnlockedCompletedActivity() =
        this.activities.lastOrNull { it.isUnlocked && it.isCompleted }
}