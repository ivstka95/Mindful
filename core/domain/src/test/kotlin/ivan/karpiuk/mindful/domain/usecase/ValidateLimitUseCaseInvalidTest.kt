package ivan.karpiuk.mindful.domain.usecase

import ivan.karpiuk.mindful.domain.model.ValidationResult
import ivan.karpiuk.mindful.domain.testing.FakeLimitsRepository
import ivan.karpiuk.mindful.domain.testing.FakeSettingsRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

class ValidateLimitUseCaseInvalidTest {
    private lateinit var limits: FakeLimitsRepository
    private lateinit var settings: FakeSettingsRepository
    private lateinit var useCase: ValidateLimitUseCase

    @Before
    fun setUp() {
        limits = FakeLimitsRepository()
        settings = FakeSettingsRepository()
        useCase = ValidateLimitUseCase(limits, settings)
    }

    private suspend fun assertInvalid(
        packageName: String,
        duration: Duration,
    ): ValidationResult.Invalid {
        val result = useCase(packageName, duration)
        assertTrue("expected Invalid but was $result", result is ValidationResult.Invalid)
        return result as ValidationResult.Invalid
    }

    @Test
    fun `zero duration is invalid`() =
        runTest {
            assertInvalid("com.example.app", Duration.ZERO)
        }

    @Test
    fun `negative duration is invalid`() =
        runTest {
            assertInvalid("com.example.app", (-1).minutes)
        }

    @Test
    fun `duration exceeding 24 hours is invalid`() =
        runTest {
            assertInvalid("com.example.app", 24.hours + 1.seconds)
        }

    @Test
    fun `free tier user adding app beyond limit is invalid`() =
        runTest {
            repeat(settings.freeTierMaxApps) { i ->
                limits.preset("com.app.$i", 1.hours)
            }
            val result = assertInvalid("com.new.app", 30.minutes)
            assertTrue(result.reason is ValidationResult.Reason.FreeTierLimitReached)
        }
}
