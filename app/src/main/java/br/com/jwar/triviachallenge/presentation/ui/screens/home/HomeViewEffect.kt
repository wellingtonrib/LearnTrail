package br.com.jwar.triviachallenge.presentation.ui.screens.home

sealed class HomeViewEffect {
    object NavigateToSettings : HomeViewEffect()
    data class NavigateToActivity(val lessonId: String) : HomeViewEffect()
}
