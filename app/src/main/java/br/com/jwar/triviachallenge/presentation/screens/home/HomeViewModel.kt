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
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModel @Inject constructor(
    override val unitRepository: UnitRepository,
    override val activityRepository: ActivityRepository,
    private val userRepository: UserRepository,
) : ViewModel(), HomeViewReducer {

    private val _uiState = MutableStateFlow<HomeViewState>(HomeViewState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _uiEffect = Channel<HomeViewEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()

    init {
        getUnits()
    }

    fun getUnits(refresh: Boolean = false) = viewModelScope.launch {
        unitRepository.getUnits(refresh)
            .onStart { setLoadingState() }
            .flatMapLatest { units -> units.toUnitModels(refresh) }
            .catch { error -> setErrorState(error) }
            .collect { unitModels ->
                setLoadedState(unitModels)
                getUserXP()
            }
    }

    private fun getUserXP() = viewModelScope.launch {
        userRepository.getXP().collect { userXP ->
            _uiState.update { currentState ->
                reduce(currentState, HomeViewState.Action.OnUserXPUpdated(userXP))
            }
        }
    }

    private suspend fun setLoadedState(units: List<UnitModel>) {
        _uiState.update { currentState ->
            reduce(currentState, HomeViewState.Action.OnLoaded(units))
        }
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

    private suspend fun List<Unit>.toUnitModels(refresh: Boolean) = combine(
        this.map { unit ->
            activityRepository.getActivities(unit.id, refresh).map { activities ->
                UnitModel.fromUnit(unit, activities.toActivityModels())
            }.distinctUntilChanged()
        }
    ) { unitModels -> unitModels.toList() }

    private fun List<Activity>.toActivityModels() = this.map { ActivityModel.fromActivity(it) }
}