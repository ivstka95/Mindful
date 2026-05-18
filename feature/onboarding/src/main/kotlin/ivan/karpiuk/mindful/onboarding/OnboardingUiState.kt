package ivan.karpiuk.mindful.onboarding

data class OnboardingUiState(
    val step: OnboardingStep = OnboardingStep.Welcome,
)
