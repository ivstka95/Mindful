package ivan.karpiuk.mindful.domain.usecase

import ivan.karpiuk.mindful.domain.model.ValidationResult
import ivan.karpiuk.mindful.domain.repository.LimitsRepository
import ivan.karpiuk.mindful.domain.repository.SettingsRepository
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours

class ValidateLimitUseCase(
    private val limitsRepository: LimitsRepository,
    private val settingsRepository: SettingsRepository,
) {
    suspend operator fun invoke(
        packageName: String,
        proposedLimit: Duration,
    ): ValidationResult {
        if (!proposedLimit.isPositive()) {
            return ValidationResult.Invalid(ValidationResult.Reason.NonPositiveDuration)
        }
        if (proposedLimit > 24.hours) {
            return ValidationResult.Invalid(ValidationResult.Reason.ExceedsMaxDuration)
        }
        val alreadyHasLimit = limitsRepository.getLimit(packageName) != null
        if (!alreadyHasLimit && !settingsRepository.isPremium()) {
            val currentCount = limitsRepository.getAll().size
            val maxApps = settingsRepository.getFreeTierMaxApps()
            if (currentCount >= maxApps) {
                return ValidationResult.Invalid(ValidationResult.Reason.FreeTierLimitReached(maxApps))
            }
        }
        return ValidationResult.Valid
    }
}
