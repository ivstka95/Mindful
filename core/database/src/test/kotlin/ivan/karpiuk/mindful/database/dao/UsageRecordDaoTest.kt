package ivan.karpiuk.mindful.database.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import ivan.karpiuk.mindful.database.MindfulDatabase
import ivan.karpiuk.mindful.database.entity.UsageRecordEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
class UsageRecordDaoTest {
    private lateinit var db: MindfulDatabase
    private lateinit var dao: UsageRecordDao

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db =
            Room
                .inMemoryDatabaseBuilder(context, MindfulDatabase::class.java)
                .allowMainThreadQueries()
                .build()
        dao = db.usageRecordDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun `returns null for unknown record`() =
        runTest {
            assertNull(dao.get("com.example.app", "2026-05-09"))
        }

    @Test
    fun `inserts and retrieves a usage record`() =
        runTest {
            val entity =
                UsageRecordEntity(
                    packageName = "com.example.app",
                    dayKey = "2026-05-09",
                    durationMs = 600_000L,
                )
            dao.insert(entity)
            assertEquals(entity, dao.get("com.example.app", "2026-05-09"))
        }

    @Test
    fun `insert replaces existing record with same primary key`() =
        runTest {
            dao.insert(UsageRecordEntity("com.example.app", "2026-05-09", 600_000L))
            dao.insert(UsageRecordEntity("com.example.app", "2026-05-09", 1_800_000L))
            assertEquals(1_800_000L, dao.get("com.example.app", "2026-05-09")?.durationMs)
        }

    @Test
    fun `getTotalDurationMsForDay returns sum of all apps`() =
        runTest {
            dao.insert(UsageRecordEntity("com.instagram.android", "2026-05-09", 1_800_000L))
            dao.insert(UsageRecordEntity("com.zhiliaoapp.musically", "2026-05-09", 900_000L))
            assertEquals(2_700_000L, dao.getTotalDurationMsForDay("2026-05-09"))
        }

    @Test
    fun `getTotalDurationMsForDay returns 0 for day with no records`() =
        runTest {
            assertEquals(0L, dao.getTotalDurationMsForDay("2026-05-09"))
        }

    @Test
    fun `getTotalDurationMsForDay only counts records for specified day`() =
        runTest {
            dao.insert(UsageRecordEntity("com.instagram.android", "2026-05-09", 1_800_000L))
            dao.insert(UsageRecordEntity("com.instagram.android", "2026-05-10", 600_000L))
            assertEquals(1_800_000L, dao.getTotalDurationMsForDay("2026-05-09"))
        }

    @Test
    fun `deleteAllForDay removes only records for that day`() =
        runTest {
            dao.insert(UsageRecordEntity("com.example.app", "2026-05-09", 1_800_000L))
            dao.insert(UsageRecordEntity("com.example.app", "2026-05-10", 600_000L))

            dao.deleteAllForDay("2026-05-09")

            assertNull(dao.get("com.example.app", "2026-05-09"))
            assertEquals(600_000L, dao.get("com.example.app", "2026-05-10")?.durationMs)
        }

    @Test
    fun `observe emits null initially for unknown record`() =
        runTest {
            assertNull(dao.observe("com.example.app", "2026-05-09").first())
        }
}
