package ivan.karpiuk.mindful.domain.usecase

import ivan.karpiuk.mindful.domain.model.DayKey
import ivan.karpiuk.mindful.domain.repository.LimitsRepository
import ivan.karpiuk.mindful.domain.repository.UsageRepository
import kotlinx.datetime.TimeZone
import kotlin.time.Clock
import kotlin.time.Duration

class CalculateRemainingTimeUseCase(
    private val limitsRepository: LimitsRepository,
    private val usageRepository: UsageRepository,
    private val clock: Clock,
    private val timeZone: TimeZone = TimeZone.currentSystemDefault(),
) {
    suspend operator fun invoke(packageName: String): Duration? {
        val limit = limitsRepository.getLimit(packageName) ?: return null
        val dayKey = DayKey.today(clock, timeZone)
        val todayUsage = usageRepository.getTodayUsage(packageName, dayKey)
        return maxOf(Duration.ZERO, limit.dailyLimit - todayUsage)
    }
}
