package ivan.karpiuk.mindful.onboarding

import android.app.AppOpsManager
import android.content.Context
import android.os.Build
import android.os.Process
import android.provider.Settings
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PermissionChecker
    @Inject
    constructor(
        @ApplicationContext private val context: Context,
    ) {
        fun isUsageAccessGranted(): Boolean {
            val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
            val mode =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    @Suppress("DEPRECATION")
                    appOps.unsafeCheckOpNoThrow(
                        AppOpsManager.OPSTR_GET_USAGE_STATS,
                        Process.myUid(),
                        context.packageName,
                    )
                } else {
                    @Suppress("DEPRECATION")
                    appOps.checkOpNoThrow(
                        AppOpsManager.OPSTR_GET_USAGE_STATS,
                        Process.myUid(),
                        context.packageName,
                    )
                }
            return mode == AppOpsManager.MODE_ALLOWED
        }

        fun isAccessibilityServiceEnabled(): Boolean {
            val enabledServices =
                Settings.Secure.getString(
                    context.contentResolver,
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES,
                ) ?: return false
            val componentName =
                "${context.packageName}/ivan.karpiuk.mindful.accessibility.MindfulAccessibilityService"
            return enabledServices.split(":").any { it.equals(componentName, ignoreCase = true) }
        }

        fun allPermissionsGranted(): Boolean = isUsageAccessGranted() && isAccessibilityServiceEnabled()
    }
