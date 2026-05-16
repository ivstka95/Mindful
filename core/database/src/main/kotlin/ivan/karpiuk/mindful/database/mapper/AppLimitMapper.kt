package ivan.karpiuk.mindful.database.mapper

import ivan.karpiuk.mindful.database.entity.AppLimitEntity
import ivan.karpiuk.mindful.domain.model.AppLimit
import kotlin.time.Duration.Companion.milliseconds

fun AppLimitEntity.toDomain(): AppLimit =
    AppLimit(
        packageName = packageName,
        dailyLimit = dailyLimitMs.milliseconds,
    )

fun AppLimit.toEntity(): AppLimitEntity =
    AppLimitEntity(
        packageName = packageName,
        dailyLimitMs = dailyLimit.inWholeMilliseconds,
    )
