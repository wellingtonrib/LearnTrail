package br.com.jwar.triviachallenge.presentation.ui.screens.home

import br.com.jwar.triviachallenge.domain.model.Unit

sealed class HomeViewState {
    object Loading : HomeViewState()
    data class Loaded(val units: List<Unit>) : HomeViewState()
    data class Error(val error: Throwable) : HomeViewState()
}