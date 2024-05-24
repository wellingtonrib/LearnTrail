package br.com.jwar.learntrail.presentation.ui.screens.home

import br.com.jwar.learntrail.domain.model.Unit

sealed class HomeViewState {
    object Loading : HomeViewState()
    data class Loaded(val units: List<Unit>, val isRefreshing: Boolean = false) : HomeViewState()
    data class Error(val error: Throwable) : HomeViewState()
}