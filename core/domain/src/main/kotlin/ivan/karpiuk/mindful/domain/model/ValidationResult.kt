package ivan.karpiuk.mindful.domain.model

sealed interface ValidationResult {
    data object Valid : ValidationResult

    data class Invalid(
        val reason: Reason,
    ) : ValidationResult

    sealed interface Reason {
        data object NonPositiveDuration : Reason

        data object ExceedsMaxDuration : Reason

        data class FreeTierLimitReached(
            val maxApps: Int,
        ) : Reason
    }
}
