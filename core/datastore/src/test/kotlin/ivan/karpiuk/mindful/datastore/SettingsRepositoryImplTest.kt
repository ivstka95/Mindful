package ivan.karpiuk.mindful.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import app.cash.turbine.test
import ivan.karpiuk.mindful.datastore.SettingsRepositoryImpl.Companion.IS_PREMIUM_KEY
import ivan.karpiuk.mindful.datastore.SettingsRepositoryImpl.Companion.STRICT_MODE_KEY
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class SettingsRepositoryImplTest {
    @get:Rule
    val tempFolder = TemporaryFolder()

    private lateinit var dataStore: DataStore<Preferences>
    private lateinit var repo: SettingsRepositoryImpl
    private lateinit var scope: CoroutineScope

    @Before
    fun setUp() {
        scope = CoroutineScope(UnconfinedTestDispatcher() + Job())
        dataStore =
            PreferenceDataStoreFactory.create(
                scope = scope,
                produceFile = { tempFolder.newFile("test.preferences_pb") },
            )
        repo = SettingsRepositoryImpl(dataStore)
    }

    @After
    fun tearDown() {
        scope.cancel()
    }

    @Test
    fun `isPremium returns false by default`() =
        runTest {
            assertFalse(repo.isPremium())
        }

    @Test
    fun `isPremium returns true when premium flag is stored`() =
        runTest {
            dataStore.edit { it[IS_PREMIUM_KEY] = true }
            assertTrue(repo.isPremium())
        }

    @Test
    fun `isStrictModeEnabled returns false by default`() =
        runTest {
            assertFalse(repo.isStrictModeEnabled())
        }

    @Test
    fun `isStrictModeEnabled returns true when strict mode flag is stored`() =
        runTest {
            dataStore.edit { it[STRICT_MODE_KEY] = true }
            assertTrue(repo.isStrictModeEnabled())
        }

    @Test
    fun `observeStrictMode emits false initially`() =
        runTest {
            repo.observeStrictMode().test {
                assertFalse(awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `observeStrictMode emits true after strict mode is enabled`() =
        runTest {
            repo.observeStrictMode().test {
                assertFalse(awaitItem())
                dataStore.edit { it[STRICT_MODE_KEY] = true }
                assertTrue(awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `getFreeTierMaxApps returns 3`() {
        assertEquals(3, repo.getFreeTierMaxApps())
    }
}
