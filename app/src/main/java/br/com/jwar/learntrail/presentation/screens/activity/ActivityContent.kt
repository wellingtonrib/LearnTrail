package br.com.jwar.learntrail.presentation.screens.activity

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.jwar.learntrail.R
import br.com.jwar.learntrail.domain.model.Question
import br.com.jwar.learntrail.presentation.ui.theme.Theme

@ExperimentalMaterial3Api
@Composable
fun ActivityContent(
    state: ActivityViewState.Loaded,
    onIntent: (ActivityViewIntent) -> Unit,
) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val userMessage = remember(state.userMessages) { state.userMessages.firstOrNull() }

    LaunchedEffect(userMessage) {
        userMessage?.let { message ->
            snackbarHostState.showSnackbar(message = message.text.asString(context))
            onIntent(ActivityViewIntent.MessageShown(message))
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                userMessage?.getColor()?.let { messageColor ->
                    Snackbar(snackbarData = data, containerColor = messageColor)
                }
            }
        },
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Text(text = state.progress)
                        Text(text = "❤️ ${state.attemptsLeft}")
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { onIntent(ActivityViewIntent.Finish) }) {
                        Icon(Icons.Outlined.Close, stringResource(R.string.action_close))
                    }
                }
            )
        }
    ) { padding ->
        if (state.isFinished) {
            Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (state.isSucceeded) {
                        Text(text = "Success")
                    } else {
                        Text(text = "Failed")
                    }
                    Text(text = "${state.points}")
                    Button(onClick = { onIntent(ActivityViewIntent.Finish) }) {
                        Text(text = stringResource(R.string.action_finish))
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier.padding(padding),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(text = state.currentQuestion.question)
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(state.currentQuestion.answers) { answer ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, state.getAnswerColor(answer)),
                            onClick = {
                                onIntent(ActivityViewIntent.SelectAnswer(answer))
                            }
                        ) {
                            Text(
                                modifier = Modifier.padding(16.dp),
                                text = answer,
                                fontWeight = state.getAnswerTextStyle(answer),
                            )
                        }
                    }
                }
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    if (state.isResultShown) {
                        Button(onClick = { onIntent(ActivityViewIntent.Next) }) {
                            Text(text = stringResource(R.string.action_continue))
                        }
                    } else {
                        Button(
                            onClick = {
                                onIntent(ActivityViewIntent.CheckAnswer)
                            },
                            enabled = state.selectedAnswer != null
                        ) {
                            Text(text = stringResource(R.string.action_check))
                        }
                    }
                }
            }
        }
    }
}

@ExperimentalMaterial3Api
@Preview(showBackground = true)
@Composable
fun GreetingChallengeScreen() {
    Theme {
        ActivityContent(
            ActivityViewState.Loaded(
                activityId = "1",
                questions = listOf(
                    Question(
                        id = "1",
                        activityId = "1",
                        correctAnswer = "Correct",
                        difficulty = "",
                        answers = listOf(),
                        question = "Question",
                        type = ""
                    )
                ),
                currentQuestion = Question(
                    id = "1",
                    activityId = "1",
                    correctAnswer = "Correct",
                    difficulty = "",
                    answers = listOf(),
                    question = "Question",
                    type = ""
                ),
                selectedAnswer = null,
                attemptsLeft = 3,
                points = 0,
                progress = "1/3",
                isResultShown = false,
                isFinished = false,
                isSucceeded = false,
            )
        ) {}
    }
}