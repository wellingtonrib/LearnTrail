package br.com.jwar.triviachallenge.presentation.screens.home

import br.com.jwar.triviachallenge.presentation.model.UnitModel

sealed class HomeViewState {
    object Loading : HomeViewState()
    data class Loaded(
        val units: List<UnitModel>,
        val userXP: Int = 0,
        val isRefreshing: Boolean = false
    ) : HomeViewState()
    data class Error(val error: Throwable) : HomeViewState()
}

