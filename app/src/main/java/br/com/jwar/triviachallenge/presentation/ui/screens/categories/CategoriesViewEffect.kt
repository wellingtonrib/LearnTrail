package br.com.jwar.triviachallenge.presentation.ui.screens.categories

sealed class CategoriesViewEffect {
    data class NavigateToChallenge(val categoryId: String) : CategoriesViewEffect()
}
