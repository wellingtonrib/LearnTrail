package br.com.jwar.learntrail.presentation.screens.home

import br.com.jwar.learntrail.presentation.model.UnitModel

sealed class HomeViewState {
    object Loading : HomeViewState()
    data class Loaded(
        val units: List<UnitModel>,
        val userXP: Int = 0,
        val isRefreshing: Boolean = false
    ) : HomeViewState()
    data class Error(val error: Throwable) : HomeViewState()

    fun asLoadedState() = this as? Loaded
    fun asErrorState() = this as? Error
}

