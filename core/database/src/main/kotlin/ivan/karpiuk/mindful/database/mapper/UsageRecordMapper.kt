package ivan.karpiuk.mindful.database.mapper

import ivan.karpiuk.mindful.database.entity.UsageRecordEntity
import ivan.karpiuk.mindful.domain.model.DayKey
import ivan.karpiuk.mindful.domain.model.UsageRecord
import kotlin.time.Duration.Companion.milliseconds

fun UsageRecordEntity.toDomain(): UsageRecord =
    UsageRecord(
        packageName = packageName,
        dayKey = DayKey(dayKey),
        duration = durationMs.milliseconds,
    )
