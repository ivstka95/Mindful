package ivan.karpiuk.mindful.domain.usecase

import ivan.karpiuk.mindful.domain.model.DayKey
import ivan.karpiuk.mindful.domain.repository.UsageRepository
import kotlinx.datetime.TimeZone
import kotlin.time.Clock
import kotlin.time.Duration

class ConsumeUsageUseCase(
    private val usageRepository: UsageRepository,
    private val clock: Clock,
    private val timeZone: TimeZone = TimeZone.currentSystemDefault(),
) {
    suspend operator fun invoke(
        packageName: String,
        elapsed: Duration,
    ) {
        require(elapsed.isPositive()) { "elapsed must be positive" }
        val dayKey = DayKey.today(clock, timeZone)
        usageRepository.recordUsage(packageName, dayKey, elapsed)
    }
}
