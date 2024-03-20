package br.com.jwar.triviachallenge.presentation.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.jwar.triviachallenge.R
import br.com.jwar.triviachallenge.domain.model.Unit
import br.com.jwar.triviachallenge.domain.model.Lesson
import br.com.jwar.triviachallenge.presentation.ui.theme.TriviaChallengeTheme

@ExperimentalMaterial3Api
@Composable
fun HomeScreen(
    categories: List<Unit>,
    onNavigateToSettings: () -> kotlin.Unit,
    onNavigateToHome: (String, String) -> kotlin.Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.title_home)) },
                actions = {
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(Icons.Outlined.Settings, stringResource(R.string.title_settings))
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(horizontal = 16.dp),
            contentPadding = padding,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            items(categories) { category ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        modifier = Modifier.padding(16.dp),
                        text = category.name
                    )
                }
                category.lessons.forEachIndexed { index, lesson ->
                    Card(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        onClick = { onNavigateToHome(category.id, lesson.id) }
                    ) {
                        Text(
                            modifier = Modifier.padding(16.dp),
                            text = stringResource(id = R.string.label_lesson, index + 1)
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    TriviaChallengeTheme {
        HomeScreen(
            categories = listOf(
                Unit(
                    id = "1",
                    name = "Unit 1",
                    lessons = listOf(
                        Lesson(id = "1", name = "Lesson 1"),
                        Lesson(id = "2", name = "Lesson 2"),
                        Lesson(id = "3", name = "Lesson 3"),
                    )
                ),
                Unit(
                    id = "2",
                    name = "Unit 2",
                    lessons = listOf(
                        Lesson(id = "4", name = "Lesson 1"),
                        Lesson(id = "5", name = "Lesson 2"),
                        Lesson(id = "6", name = "Lesson 3"),
                    )
                ),
                Unit(
                    id = "3",
                    name = "Unit 3",
                    lessons = listOf(
                        Lesson(id = "7", name = "Lesson 1"),
                        Lesson(id = "8", name = "Lesson 2"),
                        Lesson(id = "9", name = "Lesson 3"),
                    )
                ),
            ),
            onNavigateToSettings = {},
            onNavigateToHome = { _, _ -> }
        )
    }
}