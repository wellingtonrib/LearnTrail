package br.com.jwar.triviachallenge.presentation.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.jwar.triviachallenge.domain.repositories.UnitRepository
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
    private val categoriesRepository: UnitRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeViewState>(HomeViewState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _uiEffect = Channel<HomeViewEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()

    fun getUnits() = viewModelScope.launch {
        categoriesRepository.getUnits()
            .onStart { _uiState.update { HomeViewState.Loading } }
            .catch { error -> _uiState.update { HomeViewState.Error(error) } }
            .collect { units -> _uiState.update { HomeViewState.Loaded(units) } }
    }

    fun onNavigateToSettings() = viewModelScope.launch {
        _uiEffect.send(HomeViewEffect.NavigateToSettings)
    }

    fun onNavigateToChallenge(unitId: String, activityId: String) = viewModelScope.launch {
        _uiEffect.send(HomeViewEffect.NavigateToHome(unitId, activityId))
    }

}