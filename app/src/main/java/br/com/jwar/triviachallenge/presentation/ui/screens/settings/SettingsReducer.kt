package br.com.jwar.triviachallenge.presentation.ui.screens.settings

import br.com.jwar.triviachallenge.R
import br.com.jwar.triviachallenge.data.services.translator.Language
import br.com.jwar.triviachallenge.data.services.translator.TranslatorService
import br.com.jwar.triviachallenge.presentation.utils.UIMessage
import br.com.jwar.triviachallenge.presentation.utils.UIText

interface SettingsReducer {

    val translatorService: TranslatorService

    suspend fun reduce(state: SettingsViewState, action: SettingsViewState.Action): SettingsViewState {
        return when (action) {
            is SettingsViewState.Action.SetTargetLanguage -> {
                onSetTargetLanguage(state, action.language)
            }
        }
    }

    private suspend fun onSetTargetLanguage(
        state: SettingsViewState,
        language: Language,
    ): SettingsViewState {
        val result = translatorService.setTargetLanguage(language)
        return if (result.isSuccess) {
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