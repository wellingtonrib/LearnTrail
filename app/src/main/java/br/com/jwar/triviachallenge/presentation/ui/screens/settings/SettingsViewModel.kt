package br.com.jwar.triviachallenge.presentation.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.jwar.triviachallenge.data.services.translator.TranslatorService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val translatorService: TranslatorService
): ViewModel() {

    private val _uiState = MutableStateFlow(
        SettingsViewState(
            supportedLanguages = translatorService.getSupportedLanguages(),
            currentLanguage = translatorService.getLanguage()
        )
    )
    val uiState = _uiState.asStateFlow()

    fun onSelectLanguage(language: String) = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true, currentLanguage = language) }
        translatorService.setLanguage(language)
        _uiState.update { it.copy(isLoading = false) }
    }
}