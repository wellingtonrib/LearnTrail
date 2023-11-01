package br.com.jwar.triviachallenge.presentation.ui.screens.challenge

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.jwar.triviachallenge.R
import br.com.jwar.triviachallenge.domain.model.Question
import br.com.jwar.triviachallenge.presentation.ui.theme.TriviaChallengeTheme

@ExperimentalMaterial3Api
@Composable
fun ChallengeScreen(
    nextQuestion: Question,
    selectedAnswer: String? = null,
    isResultShown: Boolean = false,
    isLastQuestion: Boolean = false,
    onSelectAnswer: (String) -> Unit,
    onCheck: () -> Unit,
    onNext: () -> Unit,
    onFinish: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = nextQuestion.category)
                },
                actions = {
                    IconButton(onClick = onFinish) {
                        Icon(Icons.Outlined.Close, stringResource(R.string.action_close))
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(text = nextQuestion.question)
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(nextQuestion.answers) { answer ->

                    val answerColor = if (isResultShown) {
                        if (answer == nextQuestion.correctAnswer) Color.Green else Color.Red
                    } else {
                        if (selectedAnswer == answer) Color.White else Color.Transparent
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
                        if (isLastQuestion) {
                            Text(text = stringResource(R.string.action_finish))
                        } else {
                            Text(text = stringResource(R.string.action_continue))
                        }
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

@ExperimentalMaterial3Api
@Preview(showBackground = true)
@Composable
fun GreetingChallengeScreen() {
    TriviaChallengeTheme {
        ChallengeScreen(
            nextQuestion = Question(
                category = "",
                correctAnswer = "",
                difficulty = "",
                answers = listOf(),
                question = "",
                type = ""
            ),
            selectedAnswer = null,
            isResultShown = false,
            onSelectAnswer = {},
            onCheck = {},
            onNext = {}
        ) {}
    }
}