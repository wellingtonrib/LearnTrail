package br.com.jwar.triviachallenge.presentation.ui.screens.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.jwar.triviachallenge.domain.repositories.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val categoriesRepository: CategoryRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<CategoriesViewState>(CategoriesViewState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _uiEffect = Channel<CategoriesViewEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()

    fun getCategories() = viewModelScope.launch {
        categoriesRepository.getCategories()
            .onStart { _uiState.update { CategoriesViewState.Loading } }
            .catch { error -> _uiState.update { CategoriesViewState.Error(error) } }
            .collect { categories -> _uiState.update { CategoriesViewState.Loaded(categories) } }
    }

    fun onSelectCategory(categoryId: String) = viewModelScope.launch {
        _uiEffect.send(CategoriesViewEffect.NavigateToChallenge(categoryId))
    }

    fun onActionSettings() = viewModelScope.launch {
        _uiEffect.send(CategoriesViewEffect.NavigateToSettings)
    }

}