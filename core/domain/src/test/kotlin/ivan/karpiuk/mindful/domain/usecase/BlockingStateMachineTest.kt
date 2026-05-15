package ivan.karpiuk.mindful.domain.usecase

import ivan.karpiuk.mindful.domain.model.BlockReason
import ivan.karpiuk.mindful.domain.model.BlockingCommand
import ivan.karpiuk.mindful.domain.model.DayKey
import ivan.karpiuk.mindful.domain.testing.FakeClock
import ivan.karpiuk.mindful.domain.testing.FakeLimitsRepository
import ivan.karpiuk.mindful.domain.testing.FakeSettingsRepository
import ivan.karpiuk.mindful.domain.testing.FakeUsageRepository
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.TimeZone
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Instant

class BlockingStateMachineTest {
    private lateinit var clock: FakeClock
    private lateinit var tz: TimeZone
    private var today = DayKey("")
    private lateinit var limits: FakeLimitsRepository
    private lateinit var usage: FakeUsageRepository
    private lateinit var settings: FakeSettingsRepository
    private lateinit var evaluateBlocking: EvaluateBlockingDecisionUseCase
    private lateinit var stateMachine: BlockingStateMachine

    @Before
    fun setUp() {
        clock = FakeClock(Instant.parse("2026-05-09T10:00:00Z"))
        tz = TimeZone.UTC
        today = DayKey.today(clock, tz)
        limits = FakeLimitsRepository()
        usage = FakeUsageRepository()
        settings = FakeSettingsRepository()
        evaluateBlocking = EvaluateBlockingDecisionUseCase(limits, usage, settings, clock, tz)
        stateMachine = BlockingStateMachine(evaluateBlocking)
    }

    @Test
    fun `returns DoNothing when allowed app comes to foreground`() =
        runTest {
            assertEquals(BlockingCommand.DoNothing, stateMachine.onAppForegrounded("com.example.app"))
        }

    @Test
    fun `returns ShowOverlay when blocked app comes to foreground`() =
        runTest {
            limits.preset("com.example.app", 30.minutes)
            usage.preset("com.example.app", 30.minutes, today)
            assertEquals(
                BlockingCommand.ShowOverlay(
                    "com.example.app",
                    BlockReason.PerAppLimitReached("com.example.app", 30),
                ),
                stateMachine.onAppForegrounded("com.example.app"),
            )
        }

    @Test
    fun `returns DismissOverlay when app goes to background`() {
        assertEquals(BlockingCommand.DismissOverlay, stateMachine.onAppBackgrounded())
    }

    @Test
    fun `tracks current foreground app after app comes to foreground`() =
        runTest {
            stateMachine.onAppForegrounded("com.example.app")
            assertEquals("com.example.app", stateMachine.getCurrentForegroundApp())
        }

    @Test
    fun `current foreground app is null after backgrounding`() =
        runTest {
            stateMachine.onAppForegrounded("com.example.app")
            stateMachine.onAppBackgrounded()
            assertNull(stateMachine.getCurrentForegroundApp())
        }

    @Test
    fun `blocked app shows overlay then dismisses when user goes to home`() =
        runTest {
            limits.preset("com.example.app", 30.minutes)
            usage.preset("com.example.app", 30.minutes, today)
            assertEquals(
                BlockingCommand.ShowOverlay(
                    "com.example.app",
                    BlockReason.PerAppLimitReached("com.example.app", 30),
                ),
                stateMachine.onAppForegrounded("com.example.app"),
            )
            assertEquals(BlockingCommand.DismissOverlay, stateMachine.onAppBackgrounded())
        }
}
