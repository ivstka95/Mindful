package ivan.karpiuk.mindful.domain.usecase

import ivan.karpiuk.mindful.domain.model.DayKey
import ivan.karpiuk.mindful.domain.repository.UsageRepository

class ResetDailyUsageUseCase(
    private val usageRepository: UsageRepository,
) {
    suspend operator fun invoke(dayKey: DayKey) {
        usageRepository.resetDay(dayKey)
    }
}
