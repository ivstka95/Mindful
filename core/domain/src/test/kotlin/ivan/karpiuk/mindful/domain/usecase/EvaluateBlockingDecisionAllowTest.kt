package ivan.karpiuk.mindful.domain.usecase

import ivan.karpiuk.mindful.domain.model.BlockingDecision
import ivan.karpiuk.mindful.domain.model.DayKey
import ivan.karpiuk.mindful.domain.testing.FakeClock
import ivan.karpiuk.mindful.domain.testing.FakeLimitsRepository
import ivan.karpiuk.mindful.domain.testing.FakeSettingsRepository
import ivan.karpiuk.mindful.domain.testing.FakeUsageRepository
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.TimeZone
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
import kotlin.time.Instant

class EvaluateBlockingDecisionAllowTest {
    private lateinit var clock: FakeClock
    private lateinit var tz: TimeZone
    private var today = DayKey("")
    private lateinit var limits: FakeLimitsRepository
    private lateinit var usage: FakeUsageRepository
    private lateinit var settings: FakeSettingsRepository
    private lateinit var useCase: EvaluateBlockingDecisionUseCase

    @Before
    fun setUp() {
        clock = FakeClock(Instant.parse("2026-05-09T10:00:00Z"))
        tz = TimeZone.UTC
        today = DayKey.today(clock, tz)
        limits = FakeLimitsRepository()
        usage = FakeUsageRepository()
        settings = FakeSettingsRepository()
        useCase = EvaluateBlockingDecisionUseCase(limits, usage, settings, clock, tz)
    }

    @Test
    fun `returns Allow when no limit is configured for the app`() =
        runTest {
            assertEquals(BlockingDecision.Allow, useCase("com.example.app"))
        }

    @Test
    fun `returns Allow when usage is under the per-app limit by 1 minute`() =
        runTest {
            limits.preset("com.example.app", 30.minutes)
            usage.preset("com.example.app", 29.minutes, today)
            assertEquals(BlockingDecision.Allow, useCase("com.example.app"))
        }

    @Test
    fun `returns Allow when usage is exactly 1 second under the limit`() =
        runTest {
            limits.preset("com.example.app", 30.minutes)
            usage.preset("com.example.app", 30.minutes - 1.seconds, today)
            assertEquals(BlockingDecision.Allow, useCase("com.example.app"))
        }

    @Test
    fun `returns Allow when a different app is queried and has no limit`() =
        runTest {
            limits.preset("com.other.app", 30.minutes)
            usage.preset("com.other.app", 45.minutes, today)
            assertEquals(BlockingDecision.Allow, useCase("com.example.app"))
        }

    @Test
    fun `returns Allow when strict mode is ON but no limit is configured for this app`() =
        runTest {
            settings.isStrictModeValue = true
            assertEquals(BlockingDecision.Allow, useCase("com.example.app"))
        }
}
