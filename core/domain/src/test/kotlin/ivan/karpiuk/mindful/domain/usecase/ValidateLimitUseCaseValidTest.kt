package ivan.karpiuk.mindful.domain.usecase

import ivan.karpiuk.mindful.domain.model.ValidationResult
import ivan.karpiuk.mindful.domain.testing.FakeLimitsRepository
import ivan.karpiuk.mindful.domain.testing.FakeSettingsRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

class ValidateLimitUseCaseValidTest {
    private lateinit var limits: FakeLimitsRepository
    private lateinit var settings: FakeSettingsRepository
    private lateinit var useCase: ValidateLimitUseCase

    @Before
    fun setUp() {
        limits = FakeLimitsRepository()
        settings = FakeSettingsRepository()
        useCase = ValidateLimitUseCase(limits, settings)
    }

    @Test
    fun `positive duration within 24 hours is valid`() =
        runTest {
            assertEquals(ValidationResult.Valid, useCase("com.example.app", 30.minutes))
        }

    @Test
    fun `exactly 24 hours is valid`() =
        runTest {
            assertEquals(ValidationResult.Valid, useCase("com.example.app", 24.hours))
        }

    @Test
    fun `updating existing limit is valid even on free tier at max`() =
        runTest {
            repeat(settings.freeTierMaxApps) { i ->
                limits.preset("com.app.$i", 1.hours)
            }
            // com.app.0 already has a limit — updating it should be valid
            assertEquals(ValidationResult.Valid, useCase("com.app.0", 2.hours))
        }

    @Test
    fun `premium user can add app beyond free tier limit`() =
        runTest {
            settings.isPremiumValue = true
            repeat(settings.freeTierMaxApps) { i ->
                limits.preset("com.app.$i", 1.hours)
            }
            assertEquals(ValidationResult.Valid, useCase("com.new.app", 1.hours))
        }
}
