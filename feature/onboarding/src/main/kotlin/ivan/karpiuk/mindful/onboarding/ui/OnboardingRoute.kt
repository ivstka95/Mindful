package ivan.karpiuk.mindful.onboarding.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import ivan.karpiuk.mindful.onboarding.OnboardingEvent
import ivan.karpiuk.mindful.onboarding.OnboardingStep
import ivan.karpiuk.mindful.onboarding.OnboardingViewModel

@Composable
fun OnboardingRoute(
    modifier: Modifier = Modifier,
    onOnboardingComplete: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(viewModel) {
        viewModel.navigationIntent.collect { intent ->
            context.startActivity(intent)
        }
    }

    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            viewModel.onEvent(OnboardingEvent.PermissionCheckRequested)
        }
    }

    when (uiState.step) {
        OnboardingStep.Welcome ->
            WelcomeScreen(
                modifier = modifier,
                onPrimaryAction = { viewModel.onEvent(OnboardingEvent.PrimaryActionClicked) },
            )
        OnboardingStep.UsageAccessExplanation ->
            UsageAccessScreen(
                modifier = modifier,
                onOpenSettings = { viewModel.onEvent(OnboardingEvent.PrimaryActionClicked) },
            )
        OnboardingStep.UsageAccessWaiting ->
            UsageAccessWaitingScreen(
                modifier = modifier,
                onOpenSettings = { viewModel.onEvent(OnboardingEvent.PrimaryActionClicked) },
            )
        OnboardingStep.AccessibilityDisclosure ->
            AccessibilityDisclosureScreen(
                modifier = modifier,
                onAgree = { viewModel.onEvent(OnboardingEvent.PrimaryActionClicked) },
                onDecline = { viewModel.onEvent(OnboardingEvent.SecondaryActionClicked) },
            )
        OnboardingStep.AccessibilityEnableWaiting ->
            AccessibilityEnableScreen(
                modifier = modifier,
                onOpenSettings = { viewModel.onEvent(OnboardingEvent.PrimaryActionClicked) },
            )
        OnboardingStep.Complete ->
            CompleteScreen(
                modifier = modifier,
                onGetStarted = onOnboardingComplete,
            )
    }
}
