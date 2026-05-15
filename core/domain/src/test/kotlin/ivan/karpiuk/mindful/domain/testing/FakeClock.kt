package ivan.karpiuk.mindful.domain.testing

import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.Instant

class FakeClock(
    private var now: Instant = Instant.parse("2025-01-01T00:00:00Z"),
) : Clock {
    override fun now(): Instant = now

    fun advanceBy(duration: Duration) {
        now += duration
    }

    fun setNow(instant: Instant) {
        now = instant
    }
}
