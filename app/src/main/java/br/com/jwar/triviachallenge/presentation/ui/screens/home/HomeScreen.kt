package br.com.jwar.triviachallenge.presentation.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.jwar.triviachallenge.R
import br.com.jwar.triviachallenge.domain.model.Lesson
import br.com.jwar.triviachallenge.domain.model.Unit
import br.com.jwar.triviachallenge.presentation.ui.theme.TriviaChallengeTheme

@OptIn(ExperimentalMaterialApi::class)
@ExperimentalMaterial3Api
@Composable
fun HomeScreen(
    units: List<Unit>,
    isRefreshing: Boolean = false,
    onNavigateToSettings: () -> kotlin.Unit,
    onNavigateToActivity: (String) -> kotlin.Unit,
    onRefresh: () -> kotlin.Unit,
) {
    val refreshing by remember { mutableStateOf(isRefreshing) }
    val refreshState = rememberPullRefreshState(refreshing, onRefresh)

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
        Box(
            modifier = Modifier.pullRefresh(state = refreshState),
        ) {
            LazyColumn(
                modifier = Modifier.padding(horizontal = 16.dp),
                contentPadding = padding,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                items(units) { unit ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(
                            modifier = Modifier.padding(16.dp),
                            text = unit.name
                        )
                    }
                    unit.lessons.forEachIndexed { lessonIndex, lesson ->
                        Card(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            onClick = { onNavigateToActivity(lesson.id) }
                        ) {
                            Text(
                                modifier = Modifier.padding(16.dp),
                                text = stringResource(id = R.string.label_lesson, lessonIndex + 1)
                            )
                        }
                    }
                }
            }
            PullRefreshIndicator(
                modifier = Modifier.align(Alignment.TopCenter),
                refreshing = refreshing,
                state = refreshState
            )
        }
    }
}

@ExperimentalMaterial3Api
@ExperimentalMaterialApi
@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    TriviaChallengeTheme {
        HomeScreen(
            units = listOf(
                Unit(
                    id = "1",
                    name = "Unit 1",
                    lessons = listOf(
                        Lesson(id = "1", name = "Lesson 1"),
                        Lesson(id = "2", name = "Lesson 2"),
                        Lesson(id = "3", name = "Lesson 3"),
                    )
                ),
            ),
            isRefreshing = false,
            onNavigateToSettings = {},
            onNavigateToActivity = {},
            onRefresh = {},
        )
    }
}