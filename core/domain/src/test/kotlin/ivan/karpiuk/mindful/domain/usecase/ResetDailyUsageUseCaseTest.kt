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
            usage.preset("com.example.app", 45.minutes, day)
            useCase(day)
            assertEquals(Duration.ZERO, usage.getTodayUsage("com.example.app", day))
        }
}
