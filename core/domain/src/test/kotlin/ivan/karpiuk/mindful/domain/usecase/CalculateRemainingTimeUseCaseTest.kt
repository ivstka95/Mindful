package ivan.karpiuk.mindful.domain.usecase

import ivan.karpiuk.mindful.domain.model.DayKey
import ivan.karpiuk.mindful.domain.testing.FakeClock
import ivan.karpiuk.mindful.domain.testing.FakeLimitsRepository
import ivan.karpiuk.mindful.domain.testing.FakeUsageRepository
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.TimeZone
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Instant

class CalculateRemainingTimeUseCaseTest {
    private lateinit var clock: FakeClock
    private lateinit var tz: TimeZone
    private var today = DayKey("")
    private lateinit var limits: FakeLimitsRepository
    private lateinit var usage: FakeUsageRepository
    private lateinit var useCase: CalculateRemainingTimeUseCase

    @Before
    fun setUp() {
        clock = FakeClock(Instant.parse("2026-05-09T10:00:00Z"))
        tz = TimeZone.UTC
        today = DayKey.today(clock, tz)
        limits = FakeLimitsRepository()
        usage = FakeUsageRepository()
        useCase = CalculateRemainingTimeUseCase(limits, usage, clock, tz)
    }

    @Test
    fun `returns null when no limit configured`() =
        runTest {
            assertNull(useCase("com.example.app"))
        }

    @Test
    fun `returns correct remaining time when under limit`() =
        runTest {
            limits.preset("com.example.app", 30.minutes)
            usage.preset("com.example.app", 20.minutes, today)
            assertEquals(10.minutes, useCase("com.example.app"))
        }

    @Test
    fun `returns Duration ZERO when usage equals limit`() =
        runTest {
            limits.preset("com.example.app", 30.minutes)
            usage.preset("com.example.app", 30.minutes, today)
            assertEquals(Duration.ZERO, useCase("com.example.app"))
        }

    @Test
    fun `returns Duration ZERO when usage exceeds limit`() =
        runTest {
            limits.preset("com.example.app", 30.minutes)
            usage.preset("com.example.app", 45.minutes, today)
            assertEquals(Duration.ZERO, useCase("com.example.app"))
        }
}
