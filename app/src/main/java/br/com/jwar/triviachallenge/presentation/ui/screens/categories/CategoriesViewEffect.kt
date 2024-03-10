package br.com.jwar.triviachallenge.presentation.ui.screens.categories

sealed class CategoriesViewEffect {
    object NavigateToSettings : CategoriesViewEffect()
    data class NavigateToChallenge(val categoryId: String, val challengeId: String) : CategoriesViewEffect()
}
