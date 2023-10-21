package br.com.jwar.triviachallenge.presentation.ui.screens.categories

import br.com.jwar.triviachallenge.domain.model.Category

sealed class CategoriesViewState {

    object Loading : CategoriesViewState()

    data class Loaded(val categories: List<Category>) : CategoriesViewState()

    data class Error(val error: Throwable) : CategoriesViewState()
}