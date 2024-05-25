package br.com.jwar.learntrail.presentation.screens.home

sealed class HomeViewIntent {
    object LoadUnits: HomeViewIntent()
    object Refresh: HomeViewIntent()
    object NavigateToSettings: HomeViewIntent()
    data class NavigateToActivity(val activityId: String): HomeViewIntent()
}