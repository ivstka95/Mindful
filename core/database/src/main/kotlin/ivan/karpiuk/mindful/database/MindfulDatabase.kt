package ivan.karpiuk.mindful.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ivan.karpiuk.mindful.database.dao.AppLimitDao
import ivan.karpiuk.mindful.database.dao.UsageRecordDao
import ivan.karpiuk.mindful.database.entity.AppLimitEntity
import ivan.karpiuk.mindful.database.entity.UsageRecordEntity

@Database(
    entities = [AppLimitEntity::class, UsageRecordEntity::class],
    version = 1,
    exportSchema = true,
)
abstract class MindfulDatabase : RoomDatabase() {
    abstract fun appLimitDao(): AppLimitDao

    abstract fun usageRecordDao(): UsageRecordDao
}
