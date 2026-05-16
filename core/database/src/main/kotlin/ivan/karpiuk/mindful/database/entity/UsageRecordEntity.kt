package ivan.karpiuk.mindful.database.entity

import androidx.room.Entity
import androidx.room.Index

@Entity(
    tableName = "usage_records",
    primaryKeys = ["packageName", "dayKey"],
    indices = [Index("dayKey")],
)
data class UsageRecordEntity(
    val packageName: String,
    val dayKey: String,
    val durationMs: Long,
)
