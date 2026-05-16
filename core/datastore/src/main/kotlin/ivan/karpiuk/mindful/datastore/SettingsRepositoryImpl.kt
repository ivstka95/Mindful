package ivan.karpiuk.mindful.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import ivan.karpiuk.mindful.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingsRepositoryImpl
    @Inject
    constructor(
        private val dataStore: DataStore<Preferences>,
    ) : SettingsRepository {
        override suspend fun isPremium(): Boolean = dataStore.data.map { it[IS_PREMIUM_KEY] ?: false }.first()

        override suspend fun isStrictModeEnabled(): Boolean = dataStore.data.map { it[STRICT_MODE_KEY] ?: false }.first()

        override fun observeStrictMode(): Flow<Boolean> = dataStore.data.map { it[STRICT_MODE_KEY] ?: false }

        override fun getFreeTierMaxApps(): Int = 3

        companion object {
            val STRICT_MODE_KEY = booleanPreferencesKey("strict_mode")
            val IS_PREMIUM_KEY = booleanPreferencesKey("is_premium")
        }
    }
