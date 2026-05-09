package ivan.karpiuk.mindful.domain.testing

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

class FakeClock(
    private var now: Instant = Instant.DISTANT_PAST,
) : Clock {
    override fun now(): Instant = now

    fun advanceBy(duration: kotlin.time.Duration) {
        now += duration
    }

    fun setNow(instant: Instant) {
        now = instant
    }
}
