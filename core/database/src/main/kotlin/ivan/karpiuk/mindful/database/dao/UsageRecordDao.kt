package ivan.karpiuk.mindful.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ivan.karpiuk.mindful.database.entity.UsageRecordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UsageRecordDao {
    @Query("SELECT * FROM usage_records WHERE packageName = :packageName AND dayKey = :dayKey")
    suspend fun get(
        packageName: String,
        dayKey: String,
    ): UsageRecordEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: UsageRecordEntity)

    @Query(
        """
        SELECT COALESCE(SUM(durationMs), 0)
        FROM usage_records
        WHERE dayKey = :dayKey
    """,
    )
    suspend fun getTotalDurationMsForDay(dayKey: String): Long

    @Query("SELECT * FROM usage_records WHERE packageName = :packageName AND dayKey = :dayKey")
    fun observe(
        packageName: String,
        dayKey: String,
    ): Flow<UsageRecordEntity?>

    @Query("DELETE FROM usage_records WHERE dayKey = :dayKey")
    suspend fun deleteAllForDay(dayKey: String)
}
