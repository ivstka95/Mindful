package ivan.karpiuk.mindful.onboarding

sealed interface OnboardingEvent {
    data object PrimaryActionClicked : OnboardingEvent

    data object SecondaryActionClicked : OnboardingEvent

    data object PermissionCheckRequested : OnboardingEvent
}
