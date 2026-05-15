package ivan.karpiuk.mindful.domain.model

import kotlin.time.Duration

data class UsageRecord(
    val packageName: String,
    val dayKey: DayKey,
    val duration: Duration,
)
