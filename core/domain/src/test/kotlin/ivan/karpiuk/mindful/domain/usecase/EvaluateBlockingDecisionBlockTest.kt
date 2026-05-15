package ivan.karpiuk.mindful.domain.usecase

import ivan.karpiuk.mindful.domain.model.BlockReason
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
import kotlin.time.Instant

class EvaluateBlockingDecisionBlockTest {
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
    fun `returns Block with PerAppLimitReached when usage equals the per-app limit exactly`() =
        runTest {
            limits.preset("com.example.app", 30.minutes)
            usage.preset("com.example.app", 30.minutes, today)
            assertEquals(
                BlockingDecision.Block(BlockReason.PerAppLimitReached("com.example.app", 30)),
                useCase("com.example.app"),
            )
        }

    @Test
    fun `returns Block with PerAppLimitReached when usage exceeds the per-app limit`() =
        runTest {
            limits.preset("com.example.app", 30.minutes)
            usage.preset("com.example.app", 31.minutes, today)
            assertEquals(
                BlockingDecision.Block(BlockReason.PerAppLimitReached("com.example.app", 30)),
                useCase("com.example.app"),
            )
        }

    @Test
    fun `returns Block with StrictModeActive when strict mode is ON and app has a limit`() =
        runTest {
            settings.isStrictModeValue = true
            limits.preset("com.example.app", 30.minutes)
            usage.preset("com.example.app", 10.minutes, today)
            assertEquals(
                BlockingDecision.Block(BlockReason.StrictModeActive),
                useCase("com.example.app"),
            )
        }

    @Test
    fun `returns Block with StrictModeActive when strict mode is ON and usage is zero`() =
        runTest {
            settings.isStrictModeValue = true
            limits.preset("com.example.app", 30.minutes)
            assertEquals(
                BlockingDecision.Block(BlockReason.StrictModeActive),
                useCase("com.example.app"),
            )
        }
}
