package ivan.karpiuk.mindful.domain.model

sealed interface BlockReason {
    data class PerAppLimitReached(
        val appPackage: String,
        val limitMinutes: Int,
    ) : BlockReason

    data object TotalLimitReached : BlockReason

    data object StrictModeActive : BlockReason
}
