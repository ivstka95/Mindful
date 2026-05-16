package ivan.karpiuk.mindful.database.repository

import ivan.karpiuk.mindful.database.dao.AppLimitDao
import ivan.karpiuk.mindful.database.mapper.toDomain
import ivan.karpiuk.mindful.database.mapper.toEntity
import ivan.karpiuk.mindful.domain.model.AppLimit
import ivan.karpiuk.mindful.domain.repository.LimitsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LimitsRepositoryImpl
    @Inject
    constructor(
        private val dao: AppLimitDao,
    ) : LimitsRepository {
        override suspend fun getLimit(packageName: String): AppLimit? = dao.getByPackageName(packageName)?.toDomain()

        override suspend fun setLimit(limit: AppLimit) {
            dao.insert(limit.toEntity())
        }

        override suspend fun removeLimit(packageName: String) {
            dao.deleteByPackageName(packageName)
        }

        override suspend fun getAll(): List<AppLimit> = dao.getAll().map { it.toDomain() }

        override fun observeAll(): Flow<List<AppLimit>> = dao.observeAll().map { list -> list.map { it.toDomain() } }
    }
