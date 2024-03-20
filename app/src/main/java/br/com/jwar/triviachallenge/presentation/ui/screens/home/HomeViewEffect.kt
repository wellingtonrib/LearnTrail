package br.com.jwar.triviachallenge.presentation.ui.screens.home

sealed class HomeViewEffect {
    object NavigateToSettings : HomeViewEffect()
    data class NavigateToHome(val unitId: String, val activityId: String) : HomeViewEffect()
}
