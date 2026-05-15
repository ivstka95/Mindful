package ivan.karpiuk.mindful.domain.repository

import app.cash.turbine.test
import ivan.karpiuk.mindful.domain.model.DayKey
import ivan.karpiuk.mindful.domain.testing.FakeUsageRepository
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

class FakeUsageRepositoryTest {
    private val repo = FakeUsageRepository()
    private val today = DayKey.of(LocalDate(2025, 6, 1))
    private val yesterday = DayKey.of(LocalDate(2025, 5, 31))

    @Test
    fun `getTodayUsage returns ZERO when nothing recorded`() =
        runTest {
            assertEquals(Duration.ZERO, repo.getTodayUsage("com.example.app", today))
        }

    @Test
    fun `preset with explicit dayKey sets usage directly`() =
        runTest {
            repo.preset("com.example.app", today, 30.minutes)
            assertEquals(30.minutes, repo.getTodayUsage("com.example.app", today))
        }

    @Test
    fun `recordUsage accumulates duration for the same package and day`() =
        runTest {
            repo.recordUsage("com.example.app", today, 10.minutes)
            repo.recordUsage("com.example.app", today, 20.minutes)
            assertEquals(30.minutes, repo.getTodayUsage("com.example.app", today))
        }

    @Test
    fun `recordUsage for different packages are independent`() =
        runTest {
            repo.recordUsage("com.a", today, 10.minutes)
            repo.recordUsage("com.b", today, 20.minutes)
            assertEquals(10.minutes, repo.getTodayUsage("com.a", today))
            assertEquals(20.minutes, repo.getTodayUsage("com.b", today))
        }

    @Test
    fun `getTotalUsageForDay sums all packages`() =
        runTest {
            repo.preset("com.a", today, 10.minutes)
            repo.preset("com.b", today, 20.minutes)
            assertEquals(30.minutes, repo.getTotalUsageForDay(today))
        }

    @Test
    fun `getTotalUsageForDay excludes other days`() =
        runTest {
            repo.preset("com.a", today, 10.minutes)
            repo.preset("com.a", yesterday, 99.minutes)
            assertEquals(10.minutes, repo.getTotalUsageForDay(today))
        }

    @Test
    fun `resetDay clears all usage for that day`() =
        runTest {
            repo.preset("com.a", today, 30.minutes)
            repo.preset("com.b", today, 15.minutes)
            repo.resetDay(today)
            assertEquals(Duration.ZERO, repo.getTodayUsage("com.a", today))
            assertEquals(Duration.ZERO, repo.getTotalUsageForDay(today))
        }

    @Test
    fun `resetDay does not affect other days`() =
        runTest {
            repo.preset("com.a", today, 10.minutes)
            repo.preset("com.a", yesterday, 20.minutes)
            repo.resetDay(today)
            assertEquals(20.minutes, repo.getTodayUsage("com.a", yesterday))
        }

    @Test
    fun `observeTodayUsage emits on record and reset`() =
        runTest {
            repo.observeTodayUsage("com.example.app", today).test {
                assertEquals(Duration.ZERO, awaitItem())
                repo.recordUsage("com.example.app", today, 15.minutes)
                assertEquals(15.minutes, awaitItem())
                repo.resetDay(today)
                assertEquals(Duration.ZERO, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }
}
