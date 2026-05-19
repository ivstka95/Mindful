package ivan.karpiuk.mindful

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import ivan.karpiuk.mindful.onboarding.ui.OnboardingRoute

@Composable
fun MainNavigation(modifier: Modifier = Modifier) {
    val backStack = rememberNavBackStack(OnboardingNavKey)

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
                entry<OnboardingNavKey> {
                    OnboardingRoute(
                        onOnboardingComplete = {
                            backStack.clear()
                            backStack.add(DashboardNavKey)
                        },
                    )
                }
                entry<DashboardNavKey> {
                    Box(Modifier.fillMaxSize()) {
                        Text(
                            text = "Dashboard — coming soon",
                            modifier = Modifier.align(Alignment.Center),
                        )
                    }
                }
            },
    )
}
