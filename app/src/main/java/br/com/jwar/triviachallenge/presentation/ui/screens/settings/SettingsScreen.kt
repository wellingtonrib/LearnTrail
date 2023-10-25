package br.com.jwar.triviachallenge.presentation.ui.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.jwar.triviachallenge.R
import br.com.jwar.triviachallenge.presentation.ui.theme.TriviaChallengeTheme

@ExperimentalMaterial3Api
@Composable
fun SettingsScreen(
    supportedLanguages: Collection<String>,
    currentLanguage: String,
    isLoading: Boolean,
    onSelectLanguage: (String) -> Unit,
) {
    var showLanguages by remember { mutableStateOf(false) }

    if (isLoading) {
        AlertDialog(
            onDismissRequest = { /*TODO*/ },
            confirmButton = { /*TODO*/ },
            text = {
                CircularProgressIndicator()
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.settings))
                },
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding)
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalAlignment = CenterVertically
            ) {
                Text(text = "Language")
                Spacer(modifier = Modifier.width(12.dp))
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Button(onClick = { showLanguages = !showLanguages }) {
                        Text(
                            text = currentLanguage.orEmpty()
                        )
                    }
                    DropdownMenu(
                        modifier = Modifier.fillMaxWidth(),
                        expanded = showLanguages,
                        onDismissRequest = { showLanguages = false }
                    ) {
                        supportedLanguages.forEach { entry ->
                            DropdownMenuItem(
                                text = { Text(entry) },
                                onClick = {
                                    showLanguages = false
                                    onSelectLanguage(entry)
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
fun previewSettingsScreen() {
    TriviaChallengeTheme {
        SettingsScreen(
            supportedLanguages = listOf( "English", "Portuguese"),
            currentLanguage = "Portuguese",
            isLoading = false,
        ) {}
    }
}