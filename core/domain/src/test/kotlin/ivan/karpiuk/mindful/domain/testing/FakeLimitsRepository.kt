package ivan.karpiuk.mindful.domain.testing

/**
 * Fake implementation of LimitsRepository for unit tests.
 * Will implement the LimitsRepository interface when it is defined
 * in file 07 (domain layer).
 *
 * Usage pattern:
 *   private val limits = FakeLimitsRepository()
 *   limits.preset("com.instagram.android", TimeLimit.PerApp(30.minutes))
 */
class FakeLimitsRepository {
    // Implement LimitsRepository interface when defined.
    // private val limits = mutableMapOf<String, TimeLimit>()
    // fun preset(packageName: String, limit: TimeLimit) { limits[packageName] = limit }
    // override suspend fun getLimit(packageName: String) = limits[packageName]
}
