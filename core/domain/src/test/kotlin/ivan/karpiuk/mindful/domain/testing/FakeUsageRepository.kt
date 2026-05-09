package ivan.karpiuk.mindful.domain.testing

/**
 * Fake implementation of UsageRepository for unit tests.
 * Will implement the UsageRepository interface when it is defined
 * in file 07 (domain layer).
 *
 * Usage pattern:
 *   private val usage = FakeUsageRepository()
 *   usage.preset("com.instagram.android", today = 30.minutes)
 */
class FakeUsageRepository {
    // Implement UsageRepository interface when defined.
    // private val records = mutableMapOf<String, Duration>()
    // fun preset(packageName: String, today: Duration) { records[packageName] = today }
    // override suspend fun getTodayUsage(packageName: String) = records[packageName] ?: Duration.ZERO
}
