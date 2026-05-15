package ivan.karpiuk.mindful.domain.usecase

import ivan.karpiuk.mindful.domain.model.BlockReason
import ivan.karpiuk.mindful.domain.model.BlockingDecision
import ivan.karpiuk.mindful.domain.model.DayKey
import ivan.karpiuk.mindful.domain.repository.LimitsRepository
import ivan.karpiuk.mindful.domain.repository.SettingsRepository
import ivan.karpiuk.mindful.domain.repository.UsageRepository
import kotlinx.datetime.TimeZone
import kotlin.time.Clock

class EvaluateBlockingDecisionUseCase(
    private val limitsRepository: LimitsRepository,
    private val usageRepository: UsageRepository,
    private val settingsRepository: SettingsRepository,
    private val clock: Clock,
    private val timeZone: TimeZone = TimeZone.currentSystemDefault(),
) {
    suspend operator fun invoke(packageName: String): BlockingDecision {
        val dayKey = DayKey.today(clock, timeZone)

        // Step 2: strict mode blocks immediately if a limit exists for this package
        if (settingsRepository.isStrictModeEnabled()) {
            val strictLimit = limitsRepository.getLimit(packageName)
            if (strictLimit != null) {
                return BlockingDecision.Block(BlockReason.StrictModeActive)
            }
        }

        // Step 3: no limit → always allow
        val limit =
            limitsRepository.getLimit(packageName)
                ?: return BlockingDecision.Allow

        // Step 4: per-app daily limit check
        val todayUsage = usageRepository.getTodayUsage(packageName, dayKey)
        if (todayUsage >= limit.dailyLimit) {
            return BlockingDecision.Block(
                BlockReason.PerAppLimitReached(
                    appPackage = packageName,
                    limitMinutes = limit.dailyLimit.inWholeMinutes.toInt(),
                ),
            )
        }

        // TODO: implement total daily limit in Phase 2 when premium is wired

        return BlockingDecision.Allow
    }
}
