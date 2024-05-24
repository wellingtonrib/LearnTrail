package br.com.jwar.triviachallenge.presentation.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.jwar.triviachallenge.R
import br.com.jwar.triviachallenge.data.services.translator.Language
import br.com.jwar.triviachallenge.data.services.translator.TranslatorService
import br.com.jwar.triviachallenge.presentation.utils.UIMessage
import br.com.jwar.triviachallenge.presentation.utils.UIText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val translatorService: TranslatorService
): ViewModel() {

    private val _uiState = MutableStateFlow<SettingsViewState>(getInitialState())
    val uiState = _uiState.asStateFlow()

    fun onIntent(intent: SettingsViewIntent) {
        when (intent) {
            is SettingsViewIntent.SelectLanguage -> onSelectLanguage(intent.language)
            is SettingsViewIntent.MessageShown -> onMessageShown(intent.uiMessage)
        }
    }

    private fun getInitialState() = SettingsViewState(
        currentLanguage = translatorService.getTargetLanguage(),
    )

    private fun onSelectLanguage(language: Language) {
        setProcessingState()
        setTargetLanguageAndUpdateState(language)
    }

    private fun onMessageShown(uiMessage: UIMessage) = _uiState.update { state ->
        state.copy(userMessages = state.userMessages - uiMessage)
    }

    private fun setProcessingState() = _uiState.update { currentState ->
        currentState.copy(isProcessing = true)
    }

    private fun setTargetLanguageAndUpdateState(language: Language) = viewModelScope.launch {
        val result = translatorService.setTargetLanguage(language)
        _uiState.update { state ->
            if (result.isSuccess) {
                state.copy(
                    isProcessing = false,
                    currentLanguage = language,
                    userMessages = state.userMessages + UIMessage(
                        text = UIText.StringResource(R.string.message_language_changed)
                    )
                )
            } else {
                state.copy(
                    isProcessing = false,
                    userMessages = state.userMessages + UIMessage(
                        text = UIText.DynamicString(result.exceptionOrNull()?.message.orEmpty())
                    )
                )
            }
        }
    }
}