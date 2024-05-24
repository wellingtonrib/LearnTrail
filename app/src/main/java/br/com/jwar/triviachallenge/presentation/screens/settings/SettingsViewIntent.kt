package br.com.jwar.triviachallenge.presentation.screens.settings

import br.com.jwar.triviachallenge.data.services.translator.Language
import br.com.jwar.triviachallenge.presentation.utils.UIMessage

sealed class SettingsViewIntent {
    data class SelectLanguage(val language: Language): SettingsViewIntent()
    data class MessageShown(val uiMessage: UIMessage): SettingsViewIntent()
}