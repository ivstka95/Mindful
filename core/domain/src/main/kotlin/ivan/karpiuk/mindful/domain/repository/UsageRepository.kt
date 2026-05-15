package ivan.karpiuk.mindful.domain.repository

import ivan.karpiuk.mindful.domain.model.DayKey
import kotlinx.coroutines.flow.Flow
import kotlin.time.Duration

interface UsageRepository {
    suspend fun getTodayUsage(
        packageName: String,
        dayKey: DayKey,
    ): Duration

    suspend fun getTotalUsageForDay(dayKey: DayKey): Duration

    suspend fun recordUsage(
        packageName: String,
        dayKey: DayKey,
        additional: Duration,
    )

    fun observeTodayUsage(
        packageName: String,
        dayKey: DayKey,
    ): Flow<Duration>

    suspend fun resetDay(dayKey: DayKey)
}
