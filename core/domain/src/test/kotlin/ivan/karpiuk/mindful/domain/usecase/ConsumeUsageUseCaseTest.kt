package ivan.karpiuk.mindful.domain.usecase

import ivan.karpiuk.mindful.domain.model.DayKey
import ivan.karpiuk.mindful.domain.testing.FakeClock
import ivan.karpiuk.mindful.domain.testing.FakeUsageRepository
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.TimeZone
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Instant

class ConsumeUsageUseCaseTest {
    private lateinit var clock: FakeClock
    private lateinit var tz: TimeZone
    private var today = DayKey("")
    private lateinit var usage: FakeUsageRepository
    private lateinit var useCase: ConsumeUsageUseCase

    @Before
    fun setUp() {
        clock = FakeClock(Instant.parse("2026-05-09T10:00:00Z"))
        tz = TimeZone.UTC
        today = DayKey.today(clock, tz)
        usage = FakeUsageRepository()
        useCase = ConsumeUsageUseCase(usage, clock, tz)
    }

    @Test
    fun `records usage for today with valid elapsed time`() =
        runTest {
            useCase("com.example.app", 10.minutes)
            assertEquals(10.minutes, usage.getTodayUsage("com.example.app", today))
        }

    @Test(expected = IllegalArgumentException::class)
    fun `throws IllegalArgumentException for zero elapsed time`() =
        runTest {
            useCase("com.example.app", 0.minutes)
        }

    @Test(expected = IllegalArgumentException::class)
    fun `throws IllegalArgumentException for negative elapsed time`() =
        runTest {
            useCase("com.example.app", (-1).minutes)
        }

    @Test
    fun `consecutive calls accumulate usage`() =
        runTest {
            useCase("com.example.app", 10.minutes)
            useCase("com.example.app", 5.minutes)
            assertEquals(15.minutes, usage.getTodayUsage("com.example.app", today))
        }
}
