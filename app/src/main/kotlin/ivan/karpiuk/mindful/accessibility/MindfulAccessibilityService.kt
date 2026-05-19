package ivan.karpiuk.mindful.accessibility

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import timber.log.Timber

class MindfulAccessibilityService : AccessibilityService() {
    override fun onServiceConnected() {
        super.onServiceConnected()
        Timber.d("MindfulAccessibilityService connected")
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        Timber.d("onAccessibilityEvent: type=%s", event.eventType)
    }

    override fun onInterrupt() = Unit
}
