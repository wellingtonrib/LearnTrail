package br.com.jwar.triviachallenge.presentation.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.jwar.triviachallenge.domain.model.Unit
import br.com.jwar.triviachallenge.domain.repositories.ActivityRepository
import br.com.jwar.triviachallenge.domain.repositories.UnitRepository
import br.com.jwar.triviachallenge.presentation.model.UnitModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val unitRepository: UnitRepository,
    private val activityRepository: ActivityRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeViewState>(HomeViewState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _uiEffect = Channel<HomeViewEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()

    fun getUnits(refresh: Boolean = false) = viewModelScope.launch {
        unitRepository.getUnits(refresh)
            .onStart { setLoadingState() }
            .catch { error -> setErrorState(error) }
            .collect { units -> setLoadedState(units) }
    }

    private fun setLoadedState(units: List<Unit>) = viewModelScope.launch {
        if (units.isNotEmpty()) {
            if (units.none { it.isUnlocked }) {
                val firstUnit = units.first()
                val firstActivity = firstUnit.activities.first()
                unitRepository.unlockUnit(firstUnit.id)
                activityRepository.unlockActivity(firstActivity.id)
            } else {
                units.lastOrNull { it.isUnlocked }?.let { lastUnlockedUnit ->
                    if (lastUnlockedUnit.activities.all { it.isCompleted }) {
                        val lastUnlockedUnitIndex = units.indexOf(lastUnlockedUnit)
                        val nextUnit = units.getOrNull(lastUnlockedUnitIndex + 1)
                        nextUnit?.let { unit ->
                            val firstActivity = unit.activities.first()
                            unitRepository.unlockUnit(unit.id)
                            activityRepository.unlockActivity(firstActivity.id)
                        }
                    } else {
                        val lastUnlockedActivity = lastUnlockedUnit.activities.lastOrNull { it.isUnlocked && it.isCompleted }
                        val lastUnlockedActivityIndex = lastUnlockedUnit.activities.indexOf(lastUnlockedActivity)
                        val nextActivity = lastUnlockedUnit.activities.getOrNull(lastUnlockedActivityIndex + 1)
                        nextActivity?.let { activity ->
                            activityRepository.unlockActivity(activity.id)
                        }
                    }
                }
            }
        }
        val unitModels = units.map { UnitModel.fromUnit(it) }
        _uiState.update { HomeViewState.Loaded(unitModels) }
    }

    private fun setErrorState(error: Throwable) {
        _uiState.update { HomeViewState.Error(error) }
    }

    private fun setLoadingState() {
        _uiState.update { currentState ->
            if (currentState is HomeViewState.Loaded) {
                currentState.copy(isRefreshing = true)
            } else {
                HomeViewState.Loading
            }
        }
    }

    fun onNavigateToSettings() = viewModelScope.launch {
        _uiEffect.send(HomeViewEffect.NavigateToSettings)
    }

    fun onNavigateToActivity(activityId: String) = viewModelScope.launch {
        _uiEffect.send(HomeViewEffect.NavigateToActivity(activityId))
    }

    fun onRefresh() = getUnits(refresh = true)
}