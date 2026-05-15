package ivan.karpiuk.mindful.domain.testing

import ivan.karpiuk.mindful.domain.model.AppLimit
import ivan.karpiuk.mindful.domain.repository.LimitsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlin.time.Duration

class FakeLimitsRepository : LimitsRepository {
    val limits = mutableMapOf<String, AppLimit>()

    fun preset(limit: AppLimit) {
        limits[limit.packageName] = limit
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
    }

    override suspend fun removeLimit(packageName: String) {
        limits.remove(packageName)
    }

    override suspend fun getAll(): List<AppLimit> = limits.values.toList()

    override fun observeAll(): Flow<List<AppLimit>> = flowOf(limits.values.toList())
}
