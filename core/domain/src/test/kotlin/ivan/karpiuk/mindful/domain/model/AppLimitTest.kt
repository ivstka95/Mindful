package ivan.karpiuk.mindful.domain.model

import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

class AppLimitTest {
    @Test
    fun `creates AppLimit with valid positive duration`() {
        val limit = AppLimit(packageName = "com.example.app", dailyLimit = 1.hours)
        assertEquals("com.example.app", limit.packageName)
        assertEquals(1.hours, limit.dailyLimit)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `throws when dailyLimit is zero`() {
        AppLimit(packageName = "com.example.app", dailyLimit = kotlin.time.Duration.ZERO)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `throws when dailyLimit is negative`() {
        AppLimit(packageName = "com.example.app", dailyLimit = -30.minutes)
    }

    @Test
    fun `one minute is accepted as positive duration`() {
        val limit = AppLimit(packageName = "com.example.app", dailyLimit = 1.minutes)
        assertEquals(1.minutes, limit.dailyLimit)
    }
}
