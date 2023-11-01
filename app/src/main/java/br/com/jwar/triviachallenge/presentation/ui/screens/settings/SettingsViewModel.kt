package br.com.jwar.triviachallenge.presentation.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.jwar.triviachallenge.R
import br.com.jwar.triviachallenge.data.services.translator.Language
import br.com.jwar.triviachallenge.data.services.translator.TranslatorService
import br.com.jwar.triviachallenge.presentation.ui.util.UIText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val translatorService: TranslatorService
): ViewModel() {

    private val _uiState = MutableStateFlow<SettingsViewState>(getIdleState())
    val uiState = _uiState.asStateFlow()

    fun onSelectLanguage(language: Language) = viewModelScope.launch(Dispatchers.IO) {
        _uiState.value = SettingsViewState.Processing
        val result = translatorService.setTargetLanguage(language)
        if (result.isSuccess) {
            _uiState.value = getIdleState(UIText.StringResource(R.string.message_language_changed))
        } else {
            _uiState.value = getIdleState(UIText.DynamicString(result.exceptionOrNull()?.message.orEmpty()))
        }
    }

    private fun getIdleState(message: UIText? = null) = SettingsViewState.Idle(
        currentLanguage = translatorService.getTargetLanguage(),
        showMessage = message
    )
}