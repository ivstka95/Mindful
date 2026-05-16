package ivan.karpiuk.mindful.database.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.test
import ivan.karpiuk.mindful.database.MindfulDatabase
import ivan.karpiuk.mindful.database.entity.AppLimitEntity
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
class AppLimitDaoTest {
    private lateinit var db: MindfulDatabase
    private lateinit var dao: AppLimitDao

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db =
            Room
                .inMemoryDatabaseBuilder(context, MindfulDatabase::class.java)
                .allowMainThreadQueries()
                .build()
        dao = db.appLimitDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun `getByPackageName returns null when package has no limit`() =
        runTest {
            assertNull(dao.getByPackageName("com.example.app"))
        }

    @Test
    fun `insert stores entity retrievable by packageName`() =
        runTest {
            val entity = AppLimitEntity(packageName = "com.example.app", dailyLimitMs = 3_600_000L)
            dao.insert(entity)
            assertEquals(entity, dao.getByPackageName("com.example.app"))
        }

    @Test
    fun `insert replaces existing entity with same packageName`() =
        runTest {
            val original = AppLimitEntity(packageName = "com.example.app", dailyLimitMs = 3_600_000L)
            val updated = AppLimitEntity(packageName = "com.example.app", dailyLimitMs = 7_200_000L)
            dao.insert(original)
            dao.insert(updated)
            assertEquals(updated, dao.getByPackageName("com.example.app"))
        }

    @Test
    fun `deleteByPackageName removes stored entity`() =
        runTest {
            val entity = AppLimitEntity(packageName = "com.example.app", dailyLimitMs = 3_600_000L)
            dao.insert(entity)
            dao.deleteByPackageName("com.example.app")
            assertNull(dao.getByPackageName("com.example.app"))
        }

    @Test
    fun `deleteByPackageName on absent packageName does not throw`() =
        runTest {
            dao.deleteByPackageName("com.not.here")
        }

    @Test
    fun `getAll returns empty list when table is empty`() =
        runTest {
            assertTrue(dao.getAll().isEmpty())
        }

    @Test
    fun `getAll returns all inserted entities`() =
        runTest {
            val a = AppLimitEntity(packageName = "com.example.a", dailyLimitMs = 1_000L)
            val b = AppLimitEntity(packageName = "com.example.b", dailyLimitMs = 2_000L)
            dao.insert(a)
            dao.insert(b)
            assertEquals(setOf(a, b), dao.getAll().toSet())
        }

    @Test
    fun `observeAll emits empty list initially`() =
        runTest {
            dao.observeAll().test {
                assertTrue(awaitItem().isEmpty())
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `observeAll emits updated list after insert`() =
        runTest {
            val entity = AppLimitEntity(packageName = "com.example.app", dailyLimitMs = 3_600_000L)
            dao.observeAll().test {
                assertTrue(awaitItem().isEmpty())
                dao.insert(entity)
                assertEquals(listOf(entity), awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `observeAll emits updated list after delete`() =
        runTest {
            val entity = AppLimitEntity(packageName = "com.example.app", dailyLimitMs = 3_600_000L)
            dao.insert(entity)
            dao.observeAll().test {
                assertEquals(listOf(entity), awaitItem())
                dao.deleteByPackageName("com.example.app")
                assertTrue(awaitItem().isEmpty())
                cancelAndIgnoreRemainingEvents()
            }
        }
}
