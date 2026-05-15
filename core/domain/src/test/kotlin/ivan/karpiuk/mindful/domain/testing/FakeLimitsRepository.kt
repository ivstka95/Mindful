package ivan.karpiuk.mindful.domain.testing

import ivan.karpiuk.mindful.domain.model.AppLimit
import ivan.karpiuk.mindful.domain.repository.LimitsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlin.time.Duration

class FakeLimitsRepository : LimitsRepository {
    val limits = mutableMapOf<String, AppLimit>()
    private val limitsState = MutableStateFlow<Map<String, AppLimit>>(emptyMap())

    fun preset(limit: AppLimit) {
        limits[limit.packageName] = limit
        limitsState.value = limits.toMap()
    }

    fun preset(
        packageName: String,
        dailyLimit: Duration,
    ) {
        preset(AppLimit(packageName, dailyLimit))
    }

    override suspend fun getLimit(packageName: String): AppLimit? = limits[packageName]

    override suspend fun setLimit(limit: AppLimit) {
        limits[limit.packageName] = limit
        limitsState.value = limits.toMap()
    }

    override suspend fun removeLimit(packageName: String) {
        limits.remove(packageName)
        limitsState.value = limits.toMap()
    }

    override suspend fun getAll(): List<AppLimit> = limits.values.toList()

    override fun observeAll(): Flow<List<AppLimit>> = limitsState.map { it.values.toList() }
}
