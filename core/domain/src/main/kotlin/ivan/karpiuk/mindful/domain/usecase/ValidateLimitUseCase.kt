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
            return ValidationResult(isValid = false, error = "Limit must be greater than zero")
        }
        if (proposedLimit > 24.hours) {
            return ValidationResult(isValid = false, error = "Limit must not exceed 24 hours")
        }
        val alreadyHasLimit = limitsRepository.getLimit(packageName) != null
        if (!alreadyHasLimit && !settingsRepository.isPremium()) {
            val currentCount = limitsRepository.getAll().size
            if (currentCount >= settingsRepository.getFreeTierMaxApps()) {
                return ValidationResult(
                    isValid = false,
                    error = "Free tier allows up to ${settingsRepository.getFreeTierMaxApps()} apps",
                )
            }
        }
        return ValidationResult(isValid = true)
    }
}
