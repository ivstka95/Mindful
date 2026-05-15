package ivan.karpiuk.mindful.domain

import ivan.karpiuk.mindful.domain.testing.FakeClock
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.time.Instant

/**
 * Verifies JUnit 4 and core test dependencies are correctly wired in :core:domain.
 * Delete once real domain tests exist.
 */
class SmokeTest {
    @Test
    fun `junit 4 is correctly configured`() {
        assertEquals("JUnit 4 works", "JUnit 4 works")
    }

    @Test
    fun `FakeClock returns configured instant`() {
        val clock = FakeClock(Instant.parse("2025-06-01T12:00:00Z"))
        assertEquals(Instant.parse("2025-06-01T12:00:00Z"), clock.now())
    }
}
