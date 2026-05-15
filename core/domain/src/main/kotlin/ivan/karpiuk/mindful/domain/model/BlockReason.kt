package ivan.karpiuk.mindful.domain.model

import kotlin.time.Duration

sealed interface BlockReason {
    data class PerAppLimitReached(
        val appPackage: String,
        val limit: Duration,
    ) : BlockReason

    data object TotalLimitReached : BlockReason

    data object StrictModeActive : BlockReason
}
