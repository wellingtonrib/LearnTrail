package br.com.jwar.learntrail.presentation.screens.settings

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@ExperimentalMaterial3Api
@Composable
fun SettingsRoute(
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle().value

    SettingsContent(
        isProcessing = state.isProcessing,
        currentLanguage = state.currentLanguage,
        userMessage = state.userMessages.firstOrNull(),
        onIntent = viewModel::onIntent,
    )
}