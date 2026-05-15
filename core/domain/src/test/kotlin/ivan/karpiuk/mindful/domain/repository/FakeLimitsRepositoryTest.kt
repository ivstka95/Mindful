package ivan.karpiuk.mindful.domain.repository

import ivan.karpiuk.mindful.domain.model.AppLimit
import ivan.karpiuk.mindful.domain.testing.FakeLimitsRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

class FakeLimitsRepositoryTest {
    private val repo = FakeLimitsRepository()

    @Test
    fun `getLimit returns null when nothing set`() =
        runTest {
            assertNull(repo.getLimit("com.example.app"))
        }

    @Test
    fun `preset with AppLimit stores limit retrievable by getLimit`() =
        runTest {
            val limit = AppLimit("com.example.app", 1.hours)
            repo.preset(limit)
            assertEquals(limit, repo.getLimit("com.example.app"))
        }

    @Test
    fun `preset with packageName and duration creates AppLimit`() =
        runTest {
            repo.preset("com.example.app", 45.minutes)
            assertEquals(AppLimit("com.example.app", 45.minutes), repo.getLimit("com.example.app"))
        }

    @Test
    fun `setLimit then getLimit returns the stored limit`() =
        runTest {
            val limit = AppLimit("com.example.app", 1.hours)
            repo.setLimit(limit)
            assertEquals(limit, repo.getLimit("com.example.app"))
        }

    @Test
    fun `removeLimit makes getLimit return null`() =
        runTest {
            repo.preset(AppLimit("com.example.app", 30.minutes))
            repo.removeLimit("com.example.app")
            assertNull(repo.getLimit("com.example.app"))
        }

    @Test
    fun `getAll returns all stored limits`() =
        runTest {
            repo.preset(AppLimit("com.a", 1.hours))
            repo.preset(AppLimit("com.b", 2.hours))
            assertEquals(setOf(AppLimit("com.a", 1.hours), AppLimit("com.b", 2.hours)), repo.getAll().toSet())
        }

    @Test
    fun `getAll returns empty list when nothing set`() =
        runTest {
            assertEquals(emptyList<AppLimit>(), repo.getAll())
        }

    @Test
    fun `observeAll emits current limits`() =
        runTest {
            repo.preset(AppLimit("com.a", 1.hours))
            repo.preset(AppLimit("com.b", 2.hours))
            assertEquals(
                setOf(AppLimit("com.a", 1.hours), AppLimit("com.b", 2.hours)),
                repo.observeAll().first().toSet(),
            )
        }

    @Test
    fun `observeAll reflects mutations after collection starts`() =
        runTest {
            repo.preset(AppLimit("com.a", 1.hours))
            val before = repo.observeAll().first().toSet()
            repo.preset(AppLimit("com.b", 2.hours))
            val after = repo.observeAll().first().toSet()
            assertEquals(setOf(AppLimit("com.a", 1.hours)), before)
            assertEquals(setOf(AppLimit("com.a", 1.hours), AppLimit("com.b", 2.hours)), after)
        }
}
