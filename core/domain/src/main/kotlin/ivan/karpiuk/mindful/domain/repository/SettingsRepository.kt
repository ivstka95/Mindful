package ivan.karpiuk.mindful.domain.repository

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    suspend fun isPremium(): Boolean

    suspend fun isStrictModeEnabled(): Boolean

    fun observeStrictMode(): Flow<Boolean>

    fun getFreeTierMaxApps(): Int
}
