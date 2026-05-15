package ivan.karpiuk.mindful.domain.repository

import ivan.karpiuk.mindful.domain.testing.FakeSettingsRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class FakeSettingsRepositoryTest {
    private val repo = FakeSettingsRepository()

    @Test
    fun `isPremium defaults to false`() =
        runTest {
            assertFalse(repo.isPremium())
        }

    @Test
    fun `isStrictModeEnabled defaults to false`() =
        runTest {
            assertFalse(repo.isStrictModeEnabled())
        }

    @Test
    fun `getFreeTierMaxApps returns 3 by default`() {
        assertEquals(3, repo.getFreeTierMaxApps())
    }

    @Test
    fun `isPremiumValue true makes isPremium return true`() =
        runTest {
            repo.isPremiumValue = true
            assertTrue(repo.isPremium())
        }

    @Test
    fun `isStrictModeValue true makes isStrictModeEnabled return true`() =
        runTest {
            repo.isStrictModeValue = true
            assertTrue(repo.isStrictModeEnabled())
        }

    @Test
    fun `observeStrictMode emits current isStrictModeValue at time of call`() =
        runTest {
            repo.isStrictModeValue = true
            assertTrue(repo.observeStrictMode().first())
        }

    @Test
    fun `freeTierMaxApps override is reflected in getFreeTierMaxApps`() {
        repo.freeTierMaxApps = 5
        assertEquals(5, repo.getFreeTierMaxApps())
    }
}
