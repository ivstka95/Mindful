package ivan.karpiuk.mindful.onboarding

import android.app.AppOpsManager
import android.content.Context
import android.os.Process
import android.provider.Settings
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Shadows

@RunWith(AndroidJUnit4::class)
class PermissionCheckerTest {
    private lateinit var checker: PermissionChecker
    private lateinit var context: Context

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
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
    fun `isUsageAccessGranted returns false when not granted`() {
        assertFalse(checker.isUsageAccessGranted())
    }

    @Test
    fun `isUsageAccessGranted returns true when granted`() {
        val appOps = context.getSystemService(AppOpsManager::class.java)
        Shadows.shadowOf(appOps).setMode(
            AppOpsManager.OPSTR_GET_USAGE_STATS,
            Process.myUid(),
            context.packageName,
            AppOpsManager.MODE_ALLOWED,
        )
        assertTrue(checker.isUsageAccessGranted())
    }

    @Test
    fun `isAccessibilityServiceEnabled returns false when not enabled`() {
        assertFalse(checker.isAccessibilityServiceEnabled())
    }

    @Test
    fun `isAccessibilityServiceEnabled returns true when service is enabled`() {
        val componentName =
            "${context.packageName}/ivan.karpiuk.mindful.accessibility.MindfulAccessibilityService"
        Settings.Secure.putString(
            context.contentResolver,
            Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES,
            componentName,
        )
        assertTrue(checker.isAccessibilityServiceEnabled())
    }

    @Test
    fun `allPermissionsGranted returns false when neither granted`() {
        assertFalse(checker.allPermissionsGranted())
    }
}
