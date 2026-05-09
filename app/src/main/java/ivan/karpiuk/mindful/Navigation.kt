package ivan.karpiuk.mindful

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import ivan.karpiuk.mindful.ui.main.MainRoute

@Composable
fun MainNavigation(modifier: Modifier = Modifier) {
    val backStack = rememberNavBackStack(Main)

    NavDisplay(
        backStack = backStack,
        modifier = modifier,
        onBack = { backStack.removeLastOrNull() },
        entryDecorators =
            listOf(
                // SaveableStateHolder MUST come first: ViewModelStoreNavEntryDecorator's init
                // requires LocalSavedStateRegistryOwner which it provides.
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator(),
            ),
        entryProvider =
            entryProvider {
                entry<Main> {
                    MainRoute(modifier = Modifier.safeDrawingPadding().padding(16.dp))
                }
            },
    )
}
