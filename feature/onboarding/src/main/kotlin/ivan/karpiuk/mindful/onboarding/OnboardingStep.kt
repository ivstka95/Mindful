package ivan.karpiuk.mindful.onboarding

sealed interface OnboardingStep {
    data object Welcome : OnboardingStep

    data object UsageAccessWaiting : OnboardingStep

    data object AccessibilityDisclosure : OnboardingStep

    data object AccessibilityEnableWaiting : OnboardingStep

    data object Complete : OnboardingStep
}
