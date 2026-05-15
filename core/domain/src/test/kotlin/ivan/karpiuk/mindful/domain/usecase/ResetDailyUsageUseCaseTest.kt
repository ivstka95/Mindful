package ivan.karpiuk.mindful.domain.usecase

import ivan.karpiuk.mindful.domain.model.DayKey
import ivan.karpiuk.mindful.domain.testing.FakeUsageRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

class ResetDailyUsageUseCaseTest {
    private lateinit var usage: FakeUsageRepository
    private lateinit var useCase: ResetDailyUsageUseCase

    @Before
    fun setUp() {
        usage = FakeUsageRepository()
        useCase = ResetDailyUsageUseCase(usage)
    }

    @Test
    fun `resets usage so getTodayUsage returns Duration ZERO for that day`() =
        runTest {
            val day = DayKey("2026-05-09")
            usage.preset("com.example.app", day, 45.minutes)
            useCase(day)
            assertEquals(Duration.ZERO, usage.getTodayUsage("com.example.app", day))
        }

    @Test
    fun `reset clears all apps on the target day but not other days`() =
        runTest {
            val today = DayKey("2026-05-09")
            val yesterday = DayKey("2026-05-08")
            usage.preset("com.a", today, 30.minutes)
            usage.preset("com.b", today, 15.minutes)
            usage.preset("com.a", yesterday, 60.minutes)
            useCase(today)
            assertEquals(Duration.ZERO, usage.getTodayUsage("com.a", today))
            assertEquals(Duration.ZERO, usage.getTodayUsage("com.b", today))
            assertEquals(60.minutes, usage.getTodayUsage("com.a", yesterday))
        }

    @Test
    fun `reset on a day with no usage is a no-op`() =
        runTest {
            val day = DayKey("2026-05-09")
            useCase(day)
            assertEquals(Duration.ZERO, usage.getTodayUsage("com.example.app", day))
        }
}
