package ivan.karpiuk.mindful.domain.testing

import ivan.karpiuk.mindful.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FakeSettingsRepository : SettingsRepository {
    var isPremiumValue = false
    var isStrictModeValue = false

    @JvmField
    var freeTierMaxApps = 3

    override suspend fun isPremium(): Boolean = isPremiumValue

    override suspend fun isStrictModeEnabled(): Boolean = isStrictModeValue

    override fun observeStrictMode(): Flow<Boolean> = MutableStateFlow(isStrictModeValue).asStateFlow()

    override fun getFreeTierMaxApps(): Int = freeTierMaxApps
}
