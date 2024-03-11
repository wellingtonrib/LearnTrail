package br.com.jwar.triviachallenge.presentation.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.jwar.triviachallenge.R
import br.com.jwar.triviachallenge.data.services.translator.Language
import br.com.jwar.triviachallenge.data.services.translator.TranslatorService
import br.com.jwar.triviachallenge.presentation.utils.UIMessage
import br.com.jwar.triviachallenge.presentation.utils.UIText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val translatorService: TranslatorService
): ViewModel() {

    private val _uiState = MutableStateFlow<SettingsViewState>(getInitialState())
    val uiState = _uiState.asStateFlow()

    private fun getInitialState() = SettingsViewState(
        currentLanguage = translatorService.getTargetLanguage(),
    )

    fun onSelectLanguage(language: Language) = viewModelScope.launch(Dispatchers.IO) {
        _uiState.update { state -> state.copy(isProcessing = true) }
        val result = translatorService.setTargetLanguage(language)
        if (result.isSuccess) {
            _uiState.update { state ->
                state.copy(
                    isProcessing = false,
                    currentLanguage = language,
                    userMessages = state.userMessages + UIMessage(
                        text = UIText.StringResource(R.string.message_language_changed)
                    )
                )
            }
        } else {
            _uiState.update { state ->
                state.copy(
                    isProcessing = false,
                    userMessages = state.userMessages + UIMessage(
                        text = UIText.DynamicString(result.exceptionOrNull()?.message.orEmpty())
                    )
                )
            }
        }
    }

    fun onMessageShown(uiMessage: UIMessage) {
        _uiState.update { state ->
            state.copy(userMessages = state.userMessages - uiMessage)
        }
    }
}