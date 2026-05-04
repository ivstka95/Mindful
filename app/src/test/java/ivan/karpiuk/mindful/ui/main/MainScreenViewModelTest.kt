package ivan.karpiuk.mindful.ui.main

import ivan.karpiuk.mindful.data.DataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MainScreenViewModelTest {
  @Test
  fun uiState_initiallyLoading() =
    runTest {
      val viewModel = MainScreenViewModel(FakeDataRepository())
      assertEquals(MainScreenUiState.Loading, viewModel.uiState.first())
    }
}

private class FakeDataRepository : DataRepository {
  override val data: Flow<List<String>> = flow { emit(listOf("Sample")) }
}
