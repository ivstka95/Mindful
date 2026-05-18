package ivan.karpiuk.mindful.onboarding

import android.provider.Settings
import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import io.mockk.every
import io.mockk.mockk
import ivan.karpiuk.mindful.testing.MainDispatcherRule
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class OnboardingViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val permissionChecker = mockk<PermissionChecker>()

    private fun viewModel(step: OnboardingStep = OnboardingStep.Welcome): OnboardingViewModel {
        val savedStateHandle =
            mockk<SavedStateHandle>(relaxed = true).also {
                every { it.get<String>("step") } returns
                    if (step == OnboardingStep.Welcome) null else step::class.simpleName
            }
        return OnboardingViewModel(permissionChecker, savedStateHandle)
    }

    @Test
    fun `initial step is Welcome`() =
        runTest {
            val vm = viewModel()
            vm.uiState.test {
                assertEquals(OnboardingStep.Welcome, awaitItem().step)
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `PrimaryActionClicked on Welcome emits ACTION_USAGE_ACCESS_SETTINGS when usage not granted`() =
        runTest {
            every { permissionChecker.isUsageAccessGranted() } returns false
            val vm = viewModel()

            vm.navigationIntent.test {
                vm.onEvent(OnboardingEvent.PrimaryActionClicked)
                assertEquals(Settings.ACTION_USAGE_ACCESS_SETTINGS, awaitItem().action)
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `PrimaryActionClicked on Welcome goes to AccessibilityDisclosure when usage granted`() =
        runTest {
            every { permissionChecker.isUsageAccessGranted() } returns true
            every { permissionChecker.isAccessibilityServiceEnabled() } returns false
            val vm = viewModel()

            vm.uiState.test {
                awaitItem() // initial Welcome
                vm.onEvent(OnboardingEvent.PrimaryActionClicked)
                assertEquals(OnboardingStep.AccessibilityDisclosure, awaitItem().step)
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `PermissionCheckRequested on UsageAccessWaiting advances when usage is granted`() =
        runTest {
            every { permissionChecker.isUsageAccessGranted() } returns true
            every { permissionChecker.isAccessibilityServiceEnabled() } returns false
            val vm = viewModel(OnboardingStep.UsageAccessWaiting)

            vm.uiState.test {
                awaitItem() // initial UsageAccessWaiting
                vm.onEvent(OnboardingEvent.PermissionCheckRequested)
                assertEquals(OnboardingStep.AccessibilityDisclosure, awaitItem().step)
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `SecondaryActionClicked on AccessibilityDisclosure returns to Welcome`() =
        runTest {
            val vm = viewModel(OnboardingStep.AccessibilityDisclosure)

            vm.uiState.test {
                awaitItem() // initial AccessibilityDisclosure
                vm.onEvent(OnboardingEvent.SecondaryActionClicked)
                assertEquals(OnboardingStep.Welcome, awaitItem().step)
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `PrimaryActionClicked on AccessibilityDisclosure goes to AccessibilityEnableWaiting`() =
        runTest {
            val vm = viewModel(OnboardingStep.AccessibilityDisclosure)

            vm.uiState.test {
                awaitItem() // initial AccessibilityDisclosure
                vm.onEvent(OnboardingEvent.PrimaryActionClicked)
                assertEquals(OnboardingStep.AccessibilityEnableWaiting, awaitItem().step)
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `PrimaryActionClicked on UsageAccessWaiting re-emits ACTION_USAGE_ACCESS_SETTINGS`() =
        runTest {
            val vm = viewModel(OnboardingStep.UsageAccessWaiting)

            vm.navigationIntent.test {
                vm.onEvent(OnboardingEvent.PrimaryActionClicked)
                assertEquals(Settings.ACTION_USAGE_ACCESS_SETTINGS, awaitItem().action)
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `PrimaryActionClicked on AccessibilityEnableWaiting re-emits ACTION_ACCESSIBILITY_SETTINGS`() =
        runTest {
            val vm = viewModel(OnboardingStep.AccessibilityEnableWaiting)

            vm.navigationIntent.test {
                vm.onEvent(OnboardingEvent.PrimaryActionClicked)
                assertEquals(Settings.ACTION_ACCESSIBILITY_SETTINGS, awaitItem().action)
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `PermissionCheckRequested on AccessibilityEnableWaiting with both granted goes to Complete`() =
        runTest {
            every { permissionChecker.isAccessibilityServiceEnabled() } returns true
            val vm = viewModel(OnboardingStep.AccessibilityEnableWaiting)

            vm.uiState.test {
                awaitItem() // initial AccessibilityEnableWaiting
                vm.onEvent(OnboardingEvent.PermissionCheckRequested)
                assertEquals(OnboardingStep.Complete, awaitItem().step)
                cancelAndConsumeRemainingEvents()
            }
        }
}
