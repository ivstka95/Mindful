package ivan.karpiuk.mindful.database.repository

import ivan.karpiuk.mindful.database.dao.UsageRecordDao
import ivan.karpiuk.mindful.database.entity.UsageRecordEntity
import ivan.karpiuk.mindful.domain.model.DayKey
import ivan.karpiuk.mindful.domain.repository.UsageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

class UsageRepositoryImpl
    @Inject
    constructor(
        private val dao: UsageRecordDao,
    ) : UsageRepository {
        override suspend fun getTodayUsage(
            packageName: String,
            dayKey: DayKey,
        ): Duration = dao.get(packageName, dayKey.value)?.durationMs?.milliseconds ?: Duration.ZERO

        override suspend fun getTotalUsageForDay(dayKey: DayKey): Duration = dao.getTotalDurationMsForDay(dayKey.value).milliseconds

        override suspend fun recordUsage(
            packageName: String,
            dayKey: DayKey,
            additional: Duration,
        ) {
            val existing = dao.get(packageName, dayKey.value)
            val newMs = (existing?.durationMs ?: 0L) + additional.inWholeMilliseconds
            dao.insert(UsageRecordEntity(packageName, dayKey.value, newMs))
        }

        override fun observeTodayUsage(
            packageName: String,
            dayKey: DayKey,
        ): Flow<Duration> =
            dao
                .observe(packageName, dayKey.value)
                .map { entity -> entity?.durationMs?.milliseconds ?: Duration.ZERO }

        override suspend fun resetDay(dayKey: DayKey) {
            dao.deleteAllForDay(dayKey.value)
        }
    }
