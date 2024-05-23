package br.com.jwar.triviachallenge.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.jwar.triviachallenge.domain.model.Activity
import br.com.jwar.triviachallenge.domain.model.Unit
import br.com.jwar.triviachallenge.domain.repositories.ActivityRepository
import br.com.jwar.triviachallenge.domain.repositories.UnitRepository
import br.com.jwar.triviachallenge.domain.repositories.UserRepository
import br.com.jwar.triviachallenge.presentation.model.ActivityModel
import br.com.jwar.triviachallenge.presentation.model.UnitModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModel @Inject constructor(
    private val unitRepository: UnitRepository,
    private val activityRepository: ActivityRepository,
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeViewState>(HomeViewState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _uiEffect = Channel<HomeViewEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()

    fun onIntent(intent: HomeViewIntent) {
        when (intent) {
            is HomeViewIntent.LoadUnits -> onLoadUnits()
            is HomeViewIntent.Refresh -> onRefresh()
            is HomeViewIntent.NavigateToSettings -> onNavigateToSettings()
            is HomeViewIntent.NavigateToActivity -> onNavigateToActivity(intent.activityId)
        }
    }

    private fun onLoadUnits(refresh: Boolean = false) = viewModelScope.launch {
        unitRepository.getUnits(refresh)
            .onStart { setLoadingState() }
            .flatMapLatest { units -> units.toUnitModels(refresh) }
            .catch { error -> setErrorState(error) }
            .collect { unitModels ->
                onUnitsLoaded(unitModels)
                getUserXP()
            }
    }

    private fun onRefresh() = onLoadUnits(refresh = true)

    private fun onNavigateToSettings() = viewModelScope.launch {
        _uiEffect.send(HomeViewEffect.NavigateToSettings)
    }

    private fun onNavigateToActivity(activityId: String) = viewModelScope.launch {
        _uiEffect.send(HomeViewEffect.NavigateToActivity(activityId))
    }

    private suspend fun onUnitsLoaded(units: List<UnitModel>) = _uiState.update {
        if (unlockUnitsOrActivitiesIfNeeded(units)) {
            HomeViewState.Loading
        } else {
            HomeViewState.Loaded(units)
        }
    }

    private fun getUserXP() = viewModelScope.launch {
        userRepository.getXP().collect { userXP ->
            _uiState.updateLoadedState { state ->
                state.copy(userXP = userXP)
            }
        }
    }

    private suspend fun unlockUnitsOrActivitiesIfNeeded(units: List<UnitModel>): Boolean {
        if (areAllUnitsLocked(units)) {
            unlockFirstUnit(units)
            return true
        }
        val lastUnlockedUnit = getLastUnlockedUnit(units)
        if (lastUnlockedUnit?.areAllActivitiesCompleted() == true) {
            unlockNextUnit(lastUnlockedUnit, units)
            return true
        }
        val lastUnlockedActivity = lastUnlockedUnit?.getLastUnlockedActivity()
        if (lastUnlockedActivity?.isCompleted == true) {
            unlockNextActivity(lastUnlockedUnit, lastUnlockedActivity)
            return true
        }
        return false
    }

    private suspend fun unlockFirstUnit(units: List<UnitModel>) {
        units.firstOrNull()?.let { firstUnit ->
            unitRepository.unlockUnit(firstUnit.id)
            unlockFirstActivity(firstUnit)
        }
    }

    private suspend fun unlockNextUnit(currentUnit: UnitModel, units: List<UnitModel>) {
        val nextLockedUnit = currentUnit.getNextLockedUnit(units)
        if (nextLockedUnit != null) {
            unitRepository.unlockUnit(nextLockedUnit.id)
            unlockFirstActivity(nextLockedUnit)
        }
    }

    private suspend fun unlockNextActivity(lastUnlockedUnit: UnitModel, lastUnlockedActivity: ActivityModel) {
        val nextLockedActivity = lastUnlockedUnit.getNextLockedActivity(lastUnlockedActivity)
        if (nextLockedActivity != null) {
            activityRepository.unlockActivity(nextLockedActivity.id)
        }
    }

    private suspend fun unlockFirstActivity(unit: UnitModel) =
        unit.activities.firstOrNull()?.let { activityRepository.unlockActivity(it.id) }

    private fun areAllUnitsLocked(units: List<UnitModel>) = units.all { !it.isUnlocked }

    private fun getLastUnlockedUnit(units: List<UnitModel>) = units.lastOrNull { it.isUnlocked }

    private fun UnitModel.areAllActivitiesCompleted() = this.activities.all { it.isCompleted }

    private fun UnitModel.getLastUnlockedActivity() =
        this.activities.lastOrNull { it.isUnlocked }

    private fun UnitModel.getNextLockedActivity(currentActivity: ActivityModel) =
        this.activities.zipWithNext().find { (current, next) ->
            current == currentActivity && next.isUnlocked.not()
        }?.second

    private fun UnitModel.getNextLockedUnit(units: List<UnitModel>) =
        units.zipWithNext().find { (current, next) ->
            current == this && next.isUnlocked.not()
        }?.second

    private suspend fun List<Unit>.toUnitModels(refresh: Boolean) = combine(
        this.map { unit ->
            activityRepository.getActivities(unit.id, refresh).map { activities ->
                UnitModel.fromUnit(unit, activities.toActivityModels())
            }.distinctUntilChanged()
        }
    ) { unitModels -> unitModels.toList() }

    private fun List<Activity>.toActivityModels() = this.map { ActivityModel.fromActivity(it) }

    private fun setErrorState(error: Throwable) = _uiState.update { HomeViewState.Error(error) }

    private fun setLoadingState() = _uiState.update { currentState ->
        if (currentState is HomeViewState.Loaded) {
            currentState.copy(isRefreshing = true)
        } else {
            HomeViewState.Loading
        }
    }
}

private fun MutableStateFlow<HomeViewState>.updateLoadedState(
    block: (HomeViewState.Loaded) -> HomeViewState
) = this.update { state -> state.asLoadedState()?.let { block(it) } ?: state }