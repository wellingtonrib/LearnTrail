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
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
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

@OptIn(ExperimentalMaterial3Api::class)
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
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        LinearProgressIndicator(
                            progress = { state.progress },
                        )
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
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (state.isFinished) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    if (state.isSucceeded) {
                        Text(
                            text = stringResource(R.string.message_success),
                            style = MaterialTheme.typography.titleLarge,
                        )
                    } else {
                        Text(
                            text = stringResource(R.string.message_failed),
                            style = MaterialTheme.typography.titleLarge,
                        )
                    }
                    Text(
                        text = "\uD83C\uDFC5 ${state.points}",
                        style = MaterialTheme.typography.titleLarge,
                    )
                    Button(onClick = { onIntent(ActivityViewIntent.Finish) }) {
                        Text(text = stringResource(R.string.action_finish))
                    }
                }
            } else {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Text(
                        text = state.currentQuestion.question,
                        style = MaterialTheme.typography.titleLarge,
                    )
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
}

@Preview(showBackground = true)
@Composable
fun ActivityScreen() {
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
                    answers = listOf(
                        "Answer 1",
                        "Answer 2",
                        "Answer 3",
                        "Answer 4",
                    ),
                    question = "Question",
                    type = ""
                ),
                selectedAnswer = null,
                attemptsLeft = 3,
                points = 0,
                progress = 0.5f,
                isResultShown = false,
                isFinished = false,
                isSucceeded = false,
            )
        ) {}
    }
}

@Preview(showBackground = true)
@Composable
fun ActivityResultScreen() {
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
                    answers = listOf(
                        "Correct",
                        "Answer 2",
                        "Answer 3",
                        "Answer 4",
                    ),
                    question = "Question",
                    type = ""
                ),
                selectedAnswer = "Correct",
                attemptsLeft = 3,
                points = 0,
                progress = 0.5f,
                isResultShown = true,
                isFinished = false,
                isSucceeded = false,
            )
        ) {}
    }
}

@Preview
@Composable
private fun ActivitySuccessScreen() {
    Theme {
        ActivityContent(
            ActivityViewState.Loaded(
                activityId = "1",
                questions = listOf(),
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
                progress = 0.5f,
                isResultShown = true,
                isFinished = true,
                isSucceeded = true,
            )
        ) {}
    }
}

@Preview
@Composable
private fun ActivityFailedScreen() {
    Theme {
        ActivityContent(
            ActivityViewState.Loaded(
                activityId = "1",
                questions = listOf(),
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
                progress = 0.5f,
                isResultShown = true,
                isFinished = true,
                isSucceeded = false,
            )
        ) {}
    }
}