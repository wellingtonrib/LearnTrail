package br.com.jwar.learntrail.presentation.screens.home

sealed class HomeViewEffect {
    object NavigateToSettings : HomeViewEffect()
    data class NavigateToActivity(val activityId: String) : HomeViewEffect()
}
