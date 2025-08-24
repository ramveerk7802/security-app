package com.rvcode.securityapp.services

import android.accessibilityservice.AccessibilityService
import android.os.Build
import android.text.TextUtils
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.rvcode.securityapp.services.util.HeuristicPhishingDetector
import com.rvcode.securityapp.services.util.UtilityMethod

import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ClipboardAccessibilityService : AccessibilityService() {

    @Inject lateinit var phishingDetector: HeuristicPhishingDetector
    @Inject lateinit var utilityMethod: UtilityMethod

    private val TAG = "ClipboardSvc"

    companion object {
        private const val PHISHING_ALERT_NOTIFICATION_ID = 12001
        private val COPY_ACTIONS = setOf("Copy", "Copy text")
    }

    // State variables for the workaround and duplication checks
    private var lastSelectedText: String? = null
    private var lastProcessedText: String? = null
    private var lastNotifiedPhishingUrl: String? = null

    override fun onServiceConnected() {
        super.onServiceConnected()
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return

        // Route logic based on Android version
        if (Build.VERSION.SDK_INT >= 35) {
            // ⭐ ANDROID 15+ LOGIC: Checks the official system clipboard overlay
            val pkg = event.packageName?.toString() ?: ""
            if (pkg == "com.android.systemui" &&
                (event.eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED ||
                        event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED)) {

                val node = event.source ?: return
                val text = collectVisibleText(node)
                if (!text.isNullOrBlank()) {
//                    Log.d(TAG, "Android 15+ SystemUI text captured: $text")
                    processCandidateText(text)
                }
            }
        } else {
            handleLegacyCopyEvent(event)
        }
    }


    private fun handleLegacyCopyEvent(event: AccessibilityEvent) {
        when (event.eventType) {
            // 1. A user selects text on the screen
            AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED -> {
                val node = event.source ?: return
                // Make sure the node actually has text and a selection
                if (node.text != null && node.textSelectionStart != node.textSelectionEnd) {
                    val selectedText = node.text.subSequence(node.textSelectionStart, node.textSelectionEnd).toString()
                    if (!selectedText.isNullOrBlank()) {
                        lastSelectedText = selectedText
//                        Log.d(TAG, "Text selected: $lastSelectedText")
                    }
                }
            }
            // 2. A user clicks something on the screen
            AccessibilityEvent.TYPE_VIEW_CLICKED -> {
                val node = event.source ?: return
                // Check if the clicked item's text or description is "Copy"
                val clickedText = node.text?.toString() ?: node.contentDescription?.toString()
                if (clickedText != null && COPY_ACTIONS.any { it.equals(clickedText, ignoreCase = true) }) {
//                    Log.d(TAG, "Copy action detected!")
                    // If we have recently selected text, process it
                    if (!lastSelectedText.isNullOrBlank()) {
                        processCandidateText(lastSelectedText!!)
                        // Clear it immediately to avoid accidental reprocessing
                        lastSelectedText = null
                    }
                }
            }
        }
    }

    private fun processCandidateText(text: String) {
        if (text == lastProcessedText) {
            return
        }
        lastProcessedText = text

        val phishingUrl = phishingDetector.findAndCheckUrl(text)

        if (!phishingUrl.isNullOrBlank()) {
            if (phishingUrl == lastNotifiedPhishingUrl) {
//                Log.d(TAG, "Already notified for this phishing URL: $phishingUrl")
                return
            }
            lastNotifiedPhishingUrl = phishingUrl
            Log.w(TAG, "PHISHING DETECTED: $phishingUrl")
            utilityMethod.showAlertNotification(
                notificationId = PHISHING_ALERT_NOTIFICATION_ID,
                title = "Phishing Alert!",
                message = "Suspicious link detected : {$phishingUrl}"
            )
        } else {
            utilityMethod.cancelNotification(PHISHING_ALERT_NOTIFICATION_ID)
            lastNotifiedPhishingUrl = null
        }
    }

    private fun collectVisibleText(root: AccessibilityNodeInfo?): String? {
        if (root == null) return null
        val sb = StringBuilder()
        fun dfs(node: AccessibilityNodeInfo?) {
            if (node == null) return
            val t = node.text?.toString()
            if (!t.isNullOrBlank()) {
                if (sb.isNotEmpty()) sb.append(' ')
                sb.append(t)
            }
            for (i in 0 until node.childCount) {
                dfs(node.getChild(i))
            }
        }
        dfs(root)
        val result = sb.toString().trim()
        return if (TextUtils.isEmpty(result)) null else result
    }

    override fun onInterrupt() {

    }

    override fun onDestroy() {
        super.onDestroy()

    }
}