package br.com.jwar.triviachallenge.presentation.ui.screens.settings

import br.com.jwar.triviachallenge.presentation.ui.util.UIText

sealed class SettingsViewState {
    object Processing : SettingsViewState()

    data class Idle(
        val supportedLanguages: Collection<String>,
        val currentLanguage: String,
        val showMessage: UIText? = null,
    ) : SettingsViewState()
}