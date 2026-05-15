package ivan.karpiuk.mindful.domain.testing

import ivan.karpiuk.mindful.domain.model.DayKey
import ivan.karpiuk.mindful.domain.repository.UsageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlin.time.Duration

class FakeUsageRepository : UsageRepository {
    val records = mutableMapOf<Pair<String, DayKey>, Duration>()
    private val ticker = MutableStateFlow(0)

    fun preset(
        packageName: String,
        dayKey: DayKey,
        duration: Duration,
    ) {
        records[packageName to dayKey] = duration
    }

    override suspend fun getTodayUsage(
        packageName: String,
        dayKey: DayKey,
    ): Duration = records[packageName to dayKey] ?: Duration.ZERO

    override suspend fun getTotalUsageForDay(dayKey: DayKey): Duration =
        records.entries
            .filter { (key, _) -> key.second == dayKey }
            .fold(Duration.ZERO) { acc, (_, dur) -> acc + dur }

    override suspend fun recordUsage(
        packageName: String,
        dayKey: DayKey,
        additional: Duration,
    ) {
        val key = packageName to dayKey
        records[key] = (records[key] ?: Duration.ZERO) + additional
        ticker.update { it + 1 }
    }

    override fun observeTodayUsage(
        packageName: String,
        dayKey: DayKey,
    ): Flow<Duration> = ticker.map { records[packageName to dayKey] ?: Duration.ZERO }

    override suspend fun resetDay(dayKey: DayKey) {
        records.keys.removeAll { (_, day) -> day == dayKey }
        ticker.update { it + 1 }
    }
}
