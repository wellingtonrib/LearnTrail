package br.com.jwar.triviachallenge.presentation.ui.screens.challenge

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.jwar.triviachallenge.R
import br.com.jwar.triviachallenge.domain.model.Question
import br.com.jwar.triviachallenge.presentation.model.UIMessage
import br.com.jwar.triviachallenge.presentation.ui.theme.TriviaChallengeTheme

@ExperimentalMaterial3Api
@Composable
fun ChallengeScreen(
    currentQuestion: Question,
    selectedAnswer: String? = null,
    attemptsLeft: Int,
    points: Int,
    progress: String,
    isResultShown: Boolean = false,
    isFinished: Boolean,
    isSucceeded: Boolean,
    userMessage: UIMessage? = null,
    onMessageShown: (UIMessage) -> Unit,
    onSelectAnswer: (String) -> Unit,
    onCheck: () -> Unit,
    onNext: () -> Unit,
    onFinish: () -> Unit,
) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(userMessage) {
        userMessage?.let { message ->
            snackbarHostState.showSnackbar(message = message.text.asString(context))
            onMessageShown(message)
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
                    Text(text = currentQuestion.category)
                },
                actions = {
                    IconButton(onClick = onFinish) {
                        Icon(Icons.Outlined.Close, stringResource(R.string.action_close))
                    }
                }
            )
        }
    ) { padding ->
        if (isFinished) {
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
                    if (isSucceeded) {
                        Text(text = "Success")
                    } else {
                        Text(text = "Failed")
                    }
                    Text(text = "$points points")
                    Button(onClick = onFinish) {
                        Text(text = stringResource(R.string.action_finish))
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier.padding(padding),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Text(text = progress)
                    Text(text = "❤️ $attemptsLeft")
                }
                Text(text = currentQuestion.question)
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(currentQuestion.answers) { answer ->

                        val answerColor = if (isResultShown) {
                            if (answer == currentQuestion.correctAnswer) Color.Green
                            else if (selectedAnswer == answer) Color.Red
                            else Color.Transparent
                        } else {
                            if (selectedAnswer == answer) Color.Blue else Color.Transparent
                        }

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, answerColor),
                            onClick = {
                                onSelectAnswer(answer)
                            }
                        ) {
                            Text(
                                modifier = Modifier.padding(16.dp),
                                text = answer,
                                fontWeight = if (currentQuestion.correctAnswer == answer)
                                    FontWeight.Bold else FontWeight.Normal

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
                    if (isResultShown) {
                        Button(onClick = onNext) {
                            Text(text = stringResource(R.string.action_continue))
                        }
                    } else {
                        Button(onClick = onCheck, enabled = selectedAnswer != null) {
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
    TriviaChallengeTheme {
        ChallengeScreen(
            currentQuestion = Question(
                category = "Category name",
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
            onSelectAnswer = {},
            onCheck = {},
            onNext = {},
            onMessageShown = {}
        ) {}
    }
}