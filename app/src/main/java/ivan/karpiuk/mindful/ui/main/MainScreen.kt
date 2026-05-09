package ivan.karpiuk.mindful.ui.main

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ivan.karpiuk.mindful.theme.MindfulTheme

@Composable
fun MainRoute(
    modifier: Modifier = Modifier,
    viewModel: MainScreenViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    when (state) {
        MainScreenUiState.Loading -> {
            // Blank
        }
        is MainScreenUiState.Success -> {
            MainScreen(data = (state as MainScreenUiState.Success).data, modifier = modifier)
        }
        is MainScreenUiState.Error -> {
            Text("Error loading data: ${(state as MainScreenUiState.Error).throwable.message}")
        }
    }
}

@Composable
@Suppress("UnstableCollections") // Scaffolding placeholder; switch to ImmutableList when real data lands.
internal fun MainScreen(
    data: List<String>,
    modifier: Modifier = Modifier,
) {
    Column(modifier) { data.forEach { Greeting(it) } }
}

@Composable
fun Greeting(
    name: String,
    modifier: Modifier = Modifier,
) {
    Text(text = "Hello $name!", modifier = modifier)
}

@Preview(showBackground = true)
@Composable
private fun MainScreenPreview() {
    MindfulTheme { MainScreen(listOf("Android")) }
}

@Preview(showBackground = true, widthDp = 340)
@Composable
private fun MainScreenPortraitPreview() {
    MindfulTheme { MainScreen(listOf("Android")) }
}
