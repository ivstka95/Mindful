package ivan.karpiuk.mindful.database.di

import android.content.Context
import androidx.room.Room
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ivan.karpiuk.mindful.database.MindfulDatabase
import ivan.karpiuk.mindful.database.dao.AppLimitDao
import ivan.karpiuk.mindful.database.dao.UsageRecordDao
import ivan.karpiuk.mindful.database.repository.LimitsRepositoryImpl
import ivan.karpiuk.mindful.database.repository.UsageRepositoryImpl
import ivan.karpiuk.mindful.domain.repository.LimitsRepository
import ivan.karpiuk.mindful.domain.repository.UsageRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
    ): MindfulDatabase = Room.databaseBuilder(context, MindfulDatabase::class.java, "mindful.db").build()

    @Provides
    fun provideAppLimitDao(db: MindfulDatabase): AppLimitDao = db.appLimitDao()

    @Provides
    fun provideUsageRecordDao(db: MindfulDatabase): UsageRecordDao = db.usageRecordDao()
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindLimitsRepository(impl: LimitsRepositoryImpl): LimitsRepository

    @Binds
    @Singleton
    abstract fun bindUsageRepository(impl: UsageRepositoryImpl): UsageRepository
}
