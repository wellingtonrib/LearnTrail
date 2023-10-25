package br.com.jwar.triviachallenge.presentation.ui.screens.settings

data class SettingsViewState(
    val supportedLanguages: Collection<String>,
    val currentLanguage: String,
    val isLoading: Boolean = false
)