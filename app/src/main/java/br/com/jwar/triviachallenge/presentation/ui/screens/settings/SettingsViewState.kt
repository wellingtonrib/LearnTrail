package br.com.jwar.triviachallenge.presentation.ui.screens.settings

import br.com.jwar.triviachallenge.data.services.translator.Language
import br.com.jwar.triviachallenge.presentation.ui.util.UIText

sealed class SettingsViewState {
    object Processing : SettingsViewState()

    data class Idle(
        val currentLanguage: Language,
        val showMessage: UIText? = null,
    ) : SettingsViewState()
}