package br.com.jwar.learntrail.presentation.screens.settings

import br.com.jwar.learntrail.data.services.translator.Language
import br.com.jwar.learntrail.presentation.utils.UIMessage

data class SettingsViewState(
    val currentLanguage: Language,
    val userMessages: List<UIMessage> = emptyList(),
    val isProcessing: Boolean = false
)