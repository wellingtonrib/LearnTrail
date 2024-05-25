package br.com.jwar.learntrail.presentation.screens.settings

import br.com.jwar.learntrail.data.services.translator.Language
import br.com.jwar.learntrail.presentation.utils.UIMessage

sealed class SettingsViewIntent {
    data class SelectLanguage(val language: Language): SettingsViewIntent()
    data class MessageShown(val uiMessage: UIMessage): SettingsViewIntent()
}