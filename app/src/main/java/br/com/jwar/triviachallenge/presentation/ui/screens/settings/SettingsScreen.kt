package br.com.jwar.triviachallenge.presentation.ui.screens.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import br.com.jwar.triviachallenge.R
import br.com.jwar.triviachallenge.data.services.translator.Language
import br.com.jwar.triviachallenge.presentation.utils.UIMessage
import br.com.jwar.triviachallenge.presentation.ui.theme.TriviaChallengeTheme

@ExperimentalMaterial3Api
@Composable
fun SettingsScreen(
    isProcessing: Boolean,
    currentLanguage: Language,
    userMessage: UIMessage? = null,
    onSelectLanguage: (Language) -> Unit,
    onMessageShown: (UIMessage) -> Unit,
) {
    var showLanguages by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(userMessage) {
        userMessage?.let { message ->
            snackbarHostState.showSnackbar(message.text.asString(context))
            onMessageShown(message)
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
                Text(text = stringResource(R.string.label_language))
                Spacer(modifier = Modifier.width(12.dp))
                Column(
                    modifier = Modifier.weight(1f)
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
                                    onSelectLanguage(language)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@ExperimentalMaterial3Api
@Preview
@Composable
fun PreviewSettingsScreen() {
    TriviaChallengeTheme {
        SettingsScreen(
            isProcessing = false,
            currentLanguage = Language.EN,
            onMessageShown = {},
            onSelectLanguage = {}
        )
    }
}