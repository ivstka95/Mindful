package ivan.karpiuk.mindful.domain.model

import ivan.karpiuk.mindful.domain.testing.FakeClock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.time.Instant

class DayKeyTest {
    @Test
    fun `of wraps LocalDate as ISO YYYY-MM-DD string`() {
        val date = LocalDate(2025, 3, 7)
        assertEquals("2025-03-07", DayKey.of(date).value)
    }

    @Test
    fun `today returns key for clock date in given timezone`() {
        // 2025-01-01T23:00:00Z is 2025-01-02 in UTC+1
        val clock = FakeClock(Instant.parse("2025-01-01T23:00:00Z"))
        val tz = TimeZone.of("Europe/Warsaw") // UTC+1 in January
        val key = DayKey.today(clock, tz)
        assertEquals("2025-01-02", key.value)
    }

    @Test
    fun `today uses system timezone by default`() {
        val clock = FakeClock(Instant.parse("2025-06-15T10:00:00Z"))
        val key = DayKey.today(clock)
        // Value is date-shaped — exact value depends on system TZ, just check format
        val regex = Regex("""\d{4}-\d{2}-\d{2}""")
        assert(regex.matches(key.value)) { "Expected ISO date but got: ${key.value}" }
    }

    @Test
    fun `DayKey equality is value based`() {
        val a = DayKey.of(LocalDate(2025, 6, 1))
        val b = DayKey.of(LocalDate(2025, 6, 1))
        assertEquals(a, b)
    }
}
