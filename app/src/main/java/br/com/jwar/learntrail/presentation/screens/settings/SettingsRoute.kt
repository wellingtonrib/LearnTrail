package br.com.jwar.learntrail.presentation.screens.settings

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@ExperimentalMaterial3Api
@Composable
fun SettingsRoute(
    viewModel: SettingsViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle().value

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is SettingsViewEffect.NavigateBack -> onNavigateBack()
            }
        }
    }

    SettingsContent(
        isProcessing = state.isProcessing,
        currentLanguage = state.currentLanguage,
        userMessage = state.userMessages.firstOrNull(),
        onIntent = viewModel::onIntent,
    )
}