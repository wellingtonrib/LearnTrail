package br.com.jwar.learntrail.presentation.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.jwar.learntrail.R
import br.com.jwar.learntrail.presentation.model.ActivityModel
import br.com.jwar.learntrail.presentation.model.UnitModel
import br.com.jwar.learntrail.presentation.ui.theme.Theme

@OptIn(ExperimentalMaterialApi::class)
@ExperimentalMaterial3Api
@Composable
fun HomeContent(
    userXP: Int,
    units: List<UnitModel>,
    isRefreshing: Boolean = false,
    onIntent: (HomeViewIntent) -> Unit,
) {
    val pullRefreshState = rememberPullRefreshState(isRefreshing, { onIntent(HomeViewIntent.Refresh) })

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "🏅$userXP") },
                actions = {
                    IconButton(onClick = { onIntent(HomeViewIntent.NavigateToSettings) }) {
                        Icon(Icons.Outlined.Settings, stringResource(R.string.title_settings))
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .pullRefresh(state = pullRefreshState)
        ) {
            LazyColumn(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                items(units) { unit ->
                    UnitComponent(unit) { activityId ->
                        onIntent(HomeViewIntent.NavigateToActivity(activityId))
                    }
                }
            }
            PullRefreshIndicator(
                modifier = Modifier.align(Alignment.TopCenter),
                refreshing = isRefreshing,
                state = pullRefreshState
            )
        }
    }
}

@ExperimentalMaterial3Api
@Composable
private fun UnitComponent(
    unit: UnitModel,
    onNavigateToActivity: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (unit.isUnlocked) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)
            }
        ),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier.padding(16.dp),
                text = unit.name
            )
            if (unit.isCompleted) {
                Image(
                    imageVector = Icons.Filled.Done,
                    contentDescription = stringResource(R.string.label_done),
                )
            }
        }
    }
    unit.activities.forEach { activity ->
        ActivityComponent(onNavigateToActivity, activity)
    }
}

@ExperimentalMaterial3Api
@Composable
private fun ActivityComponent(
    onNavigateToActivity: (String) -> Unit,
    activity: ActivityModel,
) {
    Card(
        modifier = Modifier
            .padding(top = 8.dp)
            .widthIn(min = 200.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (activity.isUnlocked) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)
            }
        ),
        onClick = {
            if (activity.isUnlocked) {
                onNavigateToActivity(activity.id)
            }
        },
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier.padding(16.dp),
                text = activity.name
            )
            if (activity.isCompleted) {
                Image(
                    imageVector = Icons.Filled.Done,
                    contentDescription = stringResource(R.string.label_done),
                )
            }
        }
    }
}

@ExperimentalMaterial3Api
@ExperimentalMaterialApi
@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    Theme {
        HomeContent(
            userXP = 100,
            units = listOf(
                UnitModel(
                    id = "1",
                    name = "Unit 1",
                    activities = listOf(
                        ActivityModel(id = "1", name = "Lesson 1", isUnlocked = true),
                        ActivityModel(id = "2", name = "Lesson 2"),
                        ActivityModel(id = "3", name = "Lesson 3"),
                    ),
                    isUnlocked = true,
                ),
                UnitModel(
                    id = "2",
                    name = "Unit 2",
                    activities = listOf(
                        ActivityModel(id = "4", name = "Lesson 1"),
                        ActivityModel(id = "5", name = "Lesson 2"),
                        ActivityModel(id = "6", name = "Lesson 3"),
                    ),
                ),
            ),
            isRefreshing = false,
        ) {}
    }
}