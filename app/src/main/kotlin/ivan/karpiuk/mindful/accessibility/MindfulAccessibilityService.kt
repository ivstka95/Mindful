package ivan.karpiuk.mindful.accessibility

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MindfulAccessibilityService : AccessibilityService() {
    override fun onServiceConnected() {
        super.onServiceConnected()
        Timber.d("MindfulAccessibilityService connected")
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        Timber.d("onAccessibilityEvent: type=%s pkg=%s", event.eventType, event.packageName)
    }

    override fun onInterrupt() = Unit
}
