package br.com.jwar.triviachallenge.presentation.ui.screens.settings

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.jwar.triviachallenge.presentation.ui.components.LoadingContent

@ExperimentalMaterial3Api
@Composable
fun SettingsRoute(
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    when (val state = viewModel.uiState.collectAsState().value) {
        is SettingsViewState.Idle -> {
            SettingsScreen(
                supportedLanguages = state.supportedLanguages,
                currentLanguage = state.currentLanguage,
                showMessage = state.showMessage,
            ) { language ->
                viewModel.onSelectLanguage(language)
            }
        }
        is SettingsViewState.Processing -> {
            LoadingContent()
        }
    }
}