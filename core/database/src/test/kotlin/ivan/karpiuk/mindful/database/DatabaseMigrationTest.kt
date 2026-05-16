package ivan.karpiuk.mindful.database

import androidx.room.testing.MigrationTestHelper
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DatabaseMigrationTest {
    @get:Rule
    val helper =
        MigrationTestHelper(
            InstrumentationRegistry.getInstrumentation(),
            MindfulDatabase::class.java,
        )

    @Test
    fun `migrates from version 1 to 2 adding dayKey index`() {
        helper.createDatabase(TEST_DB, 1).close()
        // AutoMigration 1→2 adds Index("dayKey") on usage_records; validateDroppedTables = true
        helper.runMigrationsAndValidate(TEST_DB, 2, true)
    }

    companion object {
        private const val TEST_DB = "mindful-migration-test"
    }
}
