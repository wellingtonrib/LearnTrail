package br.com.jwar.triviachallenge.presentation.ui.screens.categories

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.jwar.triviachallenge.domain.model.Category
import br.com.jwar.triviachallenge.presentation.ui.theme.TriviaChallengeTheme

@ExperimentalMaterial3Api
@Composable
fun CategoriesScreen(
    categories: List<Category>,
    onNavigateToChallenge: (String) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "Categories") })
        }
    ) { padding ->
        LazyColumn(
            contentPadding = padding,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(categories) { category ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onNavigateToChallenge(category.id) }
                ) {
                    Text(
                        modifier = Modifier.padding(16.dp),
                        text = category.name
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun GreetingCategoriesScreen() {
    TriviaChallengeTheme {
        CategoriesScreen(
            categories = listOf(
                Category(
                    id = "1",
                    name = "Category 1"
                )
            )
        ) {

        }
    }
}