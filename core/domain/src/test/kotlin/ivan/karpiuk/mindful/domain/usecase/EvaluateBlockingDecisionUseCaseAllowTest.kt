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
import org.junit.Test
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

class EvaluateBlockingDecisionUseCaseAllowTest {
    private val clock = FakeClock()
    private val tz = TimeZone.UTC
    private val today = DayKey.today(clock, tz)
    private val limits = FakeLimitsRepository()
    private val usage = FakeUsageRepository()
    private val settings = FakeSettingsRepository()
    private val useCase = EvaluateBlockingDecisionUseCase(limits, usage, settings, clock, tz)

    @Test
    fun `returns Allow when package has no limit`() =
        runTest {
            assertEquals(BlockingDecision.Allow, useCase("com.example.app"))
        }

    @Test
    fun `returns Allow when usage is zero and limit exists`() =
        runTest {
            limits.preset("com.example.app", 1.hours)
            assertEquals(BlockingDecision.Allow, useCase("com.example.app"))
        }

    @Test
    fun `returns Allow when usage is below the daily limit`() =
        runTest {
            limits.preset("com.example.app", 1.hours)
            usage.preset("com.example.app", 59.minutes, today)
            assertEquals(BlockingDecision.Allow, useCase("com.example.app"))
        }

    @Test
    fun `returns Allow when strict mode is enabled but package has no limit`() =
        runTest {
            settings.isStrictModeValue = true
            assertEquals(BlockingDecision.Allow, useCase("com.example.app"))
        }

    @Test
    fun `returns Allow for a different package that has no limit even when another package is blocked`() =
        runTest {
            limits.preset("com.blocked.app", 30.minutes)
            usage.preset("com.blocked.app", 31.minutes, today)
            assertEquals(BlockingDecision.Allow, useCase("com.other.app"))
        }
}
