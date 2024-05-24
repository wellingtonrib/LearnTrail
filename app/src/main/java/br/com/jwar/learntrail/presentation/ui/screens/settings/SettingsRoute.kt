package br.com.jwar.learntrail.presentation.ui.screens.settings

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel

@ExperimentalMaterial3Api
@Composable
fun SettingsRoute(
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val state = viewModel.uiState.collectAsState().value

    SettingsScreen(
        isProcessing = state.isProcessing,
        currentLanguage = state.currentLanguage,
        userMessage = state.userMessages.firstOrNull(),
        onMessageShown = viewModel::onMessageShown,
        onSelectLanguage = viewModel::onSelectLanguage
    )
}