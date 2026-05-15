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
import org.junit.Test
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

class EvaluateBlockingDecisionUseCaseBlockTest {
    private val clock = FakeClock()
    private val tz = TimeZone.UTC
    private val today = DayKey.today(clock, tz)
    private val limits = FakeLimitsRepository()
    private val usage = FakeUsageRepository()
    private val settings = FakeSettingsRepository()
    private val useCase = EvaluateBlockingDecisionUseCase(limits, usage, settings, clock, tz)

    @Test
    fun `returns Block with StrictModeActive when strict mode is enabled and limit exists`() =
        runTest {
            settings.isStrictModeValue = true
            limits.preset("com.example.app", 1.hours)
            assertEquals(
                BlockingDecision.Block(BlockReason.StrictModeActive),
                useCase("com.example.app"),
            )
        }

    @Test
    fun `strict mode check fires before per-app limit check`() =
        runTest {
            settings.isStrictModeValue = true
            limits.preset("com.example.app", 30.minutes)
            // usage is zero — strict mode should still fire
            assertEquals(
                BlockingDecision.Block(BlockReason.StrictModeActive),
                useCase("com.example.app"),
            )
        }

    @Test
    fun `returns Block with PerAppLimitReached when usage equals the daily limit`() =
        runTest {
            limits.preset("com.example.app", 30.minutes)
            usage.preset("com.example.app", today, 30.minutes)
            assertEquals(
                BlockingDecision.Block(BlockReason.PerAppLimitReached("com.example.app", 30.minutes)),
                useCase("com.example.app"),
            )
        }

    @Test
    fun `returns Block with PerAppLimitReached when usage exceeds the daily limit`() =
        runTest {
            limits.preset("com.example.app", 30.minutes)
            usage.preset("com.example.app", today, 31.minutes)
            assertEquals(
                BlockingDecision.Block(BlockReason.PerAppLimitReached("com.example.app", 30.minutes)),
                useCase("com.example.app"),
            )
        }

    @Test
    fun `PerAppLimitReached carries the correct package name`() =
        runTest {
            limits.preset("com.target.app", 1.hours)
            usage.preset("com.target.app", today, 61.minutes)
            val decision = useCase("com.target.app") as BlockingDecision.Block
            assertEquals("com.target.app", (decision.reason as BlockReason.PerAppLimitReached).appPackage)
        }

    @Test
    fun `PerAppLimitReached carries the full limit Duration`() =
        runTest {
            limits.preset("com.example.app", 2.hours)
            usage.preset("com.example.app", today, 2.hours)
            val decision = useCase("com.example.app") as BlockingDecision.Block
            assertEquals(2.hours, (decision.reason as BlockReason.PerAppLimitReached).limit)
        }
}
