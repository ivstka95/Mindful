package ivan.karpiuk.mindful.domain.repository

import ivan.karpiuk.mindful.domain.model.AppLimit
import kotlinx.coroutines.flow.Flow

interface LimitsRepository {
    suspend fun getLimit(packageName: String): AppLimit?

    suspend fun setLimit(limit: AppLimit)

    suspend fun removeLimit(packageName: String)

    suspend fun getAll(): List<AppLimit>

    fun observeAll(): Flow<List<AppLimit>>
}
