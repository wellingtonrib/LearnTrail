package br.com.jwar.triviachallenge.presentation.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.jwar.triviachallenge.data.services.translator.Language
import br.com.jwar.triviachallenge.data.services.translator.TranslatorService
import br.com.jwar.triviachallenge.presentation.utils.UIMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


sealed class SettingsViewIntent {
    data class SelectLanguage(val language: Language): SettingsViewIntent()
    data class MessageShown(val uiMessage: UIMessage): SettingsViewIntent()
}

@HiltViewModel
class SettingsViewModel @Inject constructor(
    override val translatorService: TranslatorService
): ViewModel(), SettingsReducer {

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
        setTargetLanguage(language)
    }

    private fun onMessageShown(uiMessage: UIMessage) = _uiState.update { state ->
        state.copy(userMessages = state.userMessages - uiMessage)
    }

    private fun setProcessingState() = _uiState.update { currentState ->
        currentState.copy(isProcessing = true)
    }

    private fun setTargetLanguage(language: Language) = viewModelScope.launch(Dispatchers.IO) {
        _uiState.update { state ->
            reduce(state, SettingsViewState.Action.SetTargetLanguage(language))
        }
    }
}