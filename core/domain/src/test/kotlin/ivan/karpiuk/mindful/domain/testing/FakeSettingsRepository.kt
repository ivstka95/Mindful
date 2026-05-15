package ivan.karpiuk.mindful.domain.testing

import ivan.karpiuk.mindful.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FakeSettingsRepository : SettingsRepository {
    var isPremiumValue = false

    private val strictModeState = MutableStateFlow(false)
    var isStrictModeValue: Boolean
        get() = strictModeState.value
        set(value) {
            strictModeState.value = value
        }

    @JvmField
    var freeTierMaxApps = 3

    override suspend fun isPremium(): Boolean = isPremiumValue

    override suspend fun isStrictModeEnabled(): Boolean = strictModeState.value

    override fun observeStrictMode(): Flow<Boolean> = strictModeState.asStateFlow()

    override fun getFreeTierMaxApps(): Int = freeTierMaxApps
}
