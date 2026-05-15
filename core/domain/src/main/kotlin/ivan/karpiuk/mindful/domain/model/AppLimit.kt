package ivan.karpiuk.mindful.domain.model

import kotlin.time.Duration

data class AppLimit(
    val packageName: String,
    val dailyLimit: Duration,
) {
    init {
        require(packageName.isNotBlank()) { "packageName must not be blank" }
        require(dailyLimit.isPositive()) { "dailyLimit must be positive, was $dailyLimit" }
    }
}
