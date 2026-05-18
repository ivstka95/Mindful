package ivan.karpiuk.mindful.onboarding

import android.app.AppOpsManager
import android.content.Context
import android.os.Process
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows

@RunWith(RobolectricTestRunner::class)
class PermissionCheckerTest {
    private lateinit var checker: PermissionChecker

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        // Robolectric defaults AppOps to MODE_ALLOWED; deny explicitly to represent not-granted state
        val appOps = context.getSystemService(AppOpsManager::class.java)
        Shadows.shadowOf(appOps).setMode(
            AppOpsManager.OPSTR_GET_USAGE_STATS,
            Process.myUid(),
            context.packageName,
            AppOpsManager.MODE_ERRORED,
        )
        checker = PermissionChecker(context)
    }

    @Test
    fun `isUsageAccessGranted returns false by default`() {
        assertFalse(checker.isUsageAccessGranted())
    }

    @Test
    fun `isAccessibilityServiceEnabled returns false by default`() {
        assertFalse(checker.isAccessibilityServiceEnabled())
    }

    @Test
    fun `allPermissionsGranted returns false when neither granted`() {
        assertFalse(checker.allPermissionsGranted())
    }
}
