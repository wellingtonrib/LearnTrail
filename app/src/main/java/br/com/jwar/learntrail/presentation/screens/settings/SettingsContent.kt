package br.com.jwar.learntrail.presentation.screens.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.jwar.learntrail.R
import br.com.jwar.learntrail.data.services.translator.Language
import br.com.jwar.learntrail.presentation.screens.activity.ActivityViewIntent
import br.com.jwar.learntrail.presentation.utils.UIMessage
import br.com.jwar.learntrail.presentation.ui.theme.Theme

@ExperimentalMaterial3Api
@Composable
fun SettingsContent(
    isProcessing: Boolean,
    currentLanguage: Language,
    userMessage: UIMessage? = null,
    onIntent: (SettingsViewIntent) -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(userMessage) {
        userMessage?.let { message ->
            snackbarHostState.showSnackbar(message.text.asString(context))
            onIntent(SettingsViewIntent.MessageShown(message))
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.title_settings))
                },
                navigationIcon = {
                    IconButton(onClick = { onIntent(SettingsViewIntent.Close) }) {
                        Icon(Icons.Outlined.Close, stringResource(R.string.action_close))
                    }
                }
            )
        }
    ) { padding ->
        if (isProcessing) {
            Box(
                modifier = Modifier.fillMaxSize(),
            ) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            return@Scaffold
        }
        Column(
            modifier = Modifier.padding(padding)
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalAlignment = CenterVertically
            ) {
                LanguageSetting(currentLanguage, onIntent)
            }
        }
    }
}

@Composable
private fun RowScope.LanguageSetting(
    currentLanguage: Language,
    onIntent: (SettingsViewIntent) -> Unit
) {
    var showLanguages by remember { mutableStateOf(false) }

    Text(text = stringResource(R.string.label_language))
    Spacer(modifier = Modifier.width(12.dp))
    Column(
        modifier = Modifier.Companion.weight(1f)
    ) {
        Button(onClick = { showLanguages = !showLanguages }) {
            Text(
                text = currentLanguage.value
            )
        }
        DropdownMenu(
            modifier = Modifier.fillMaxWidth(),
            expanded = showLanguages,
            onDismissRequest = { showLanguages = false }
        ) {
            Language.values().forEach { language ->
                DropdownMenuItem(
                    text = { Text(language.value) },
                    onClick = {
                        showLanguages = false
                        onIntent(SettingsViewIntent.SelectLanguage(language))
                    }
                )
            }
        }
    }
}

@ExperimentalMaterial3Api
@Preview
@Composable
fun PreviewSettingsScreen() {
    Theme {
        SettingsContent(
            isProcessing = false,
            currentLanguage = Language.EN,
            onIntent = {},
        )
    }
}