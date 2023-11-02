package br.com.jwar.triviachallenge.presentation.ui.screens.settings

import br.com.jwar.triviachallenge.data.services.translator.Language
import br.com.jwar.triviachallenge.presentation.model.UIMessage

data class SettingsViewState(
    val currentLanguage: Language,
    val userMessages: List<UIMessage> = emptyList(),
    val isProcessing: Boolean = false
)