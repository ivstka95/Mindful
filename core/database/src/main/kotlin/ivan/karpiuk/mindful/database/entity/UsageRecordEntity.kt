package ivan.karpiuk.mindful.database.entity

import androidx.room.Entity

@Entity(tableName = "usage_records", primaryKeys = ["packageName", "dayKey"])
data class UsageRecordEntity(
    val packageName: String,
    val dayKey: String,
    val durationMs: Long,
)
