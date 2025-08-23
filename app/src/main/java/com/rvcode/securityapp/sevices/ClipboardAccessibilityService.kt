package com.rvcode.securityapp.sevices

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.ClipboardManager
import android.os.Build
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.rvcode.securityapp.sevices.util.HeuristicPhishingDetector
import com.rvcode.securityapp.sevices.util.UtilityMethod
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ClipboardAccessibilityService : AccessibilityService() {

    @Inject
    lateinit var phishingDetector: HeuristicPhishingDetector
    @Inject
    lateinit var utilityMethod: UtilityMethod
    @Inject
    lateinit var clipboardManager: ClipboardManager

    private val TAG = "AccessibilityService"
    private val CLIPBOARD_NOTIFICATION_ID = 201
    private var lastProcessedText: String? = null

    // Use the listener which works reliably within an AccessibilityService
    private val clipboardListener = ClipboardManager.OnPrimaryClipChangedListener {
        val clipData = clipboardManager.primaryClip?.getItemAt(0)?.text?.toString()
        if (!clipData.isNullOrEmpty()) {
            Log.d(TAG, "Clip data is :$clipData")
            checkClipboardContent(clipData)
        }else{
            Log.d(TAG, "Clip data is null")
        }
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        Log.d(TAG, "Accessibility Service connected. Android Version: ${Build.VERSION.SDK_INT}")

        if(Build.VERSION.SDK_INT < 35){
            Log.d(TAG, "Using legacy method (clipboard listener).")
            clipboardManager.addPrimaryClipChangedListener(clipboardListener)
        }else{
            Log.d(TAG, "Using Android 15+ method (reading UI events).")
        }



    }


    private fun checkClipboardContent(text: String) {
        val phishingUrl = phishingDetector.findAndCheckUrl(text)
        if (phishingUrl != null) {
            Log.w(TAG, "PHISHING DETECTED! Preparing notification for: $phishingUrl")
            utilityMethod.showAlertNotification(
                notificationId = CLIPBOARD_NOTIFICATION_ID,
                title = "Phishing Alert!",
                message = "Suspicious link detected: $phishingUrl"
            )
        } else {
            Log.i(TAG, "Text checked, no phishing URL found.")
            utilityMethod.cancelNotification(notificationId = CLIPBOARD_NOTIFICATION_ID)
        }
    }


// In ClipboardAccessibilityService.kt

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        // This should now be called on Android 15
        Log.d(TAG, "EVENT RECEIVED -> Type: ${event?.eventType} | Package: ${event?.packageName}")

        if ((event?.eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED || event?.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) &&
            event.packageName == "com.android.systemui") {

            Log.d(TAG, "MATCH FOUND! This is the clipboard pop-up.")
            val sourceNode = event.source ?: return
            val capturedText = findTextInNode(sourceNode)
            Log.d(TAG, "Raw captured text: '$capturedText'")

            if (!capturedText.isNullOrBlank() && capturedText != lastProcessedText) {
                lastProcessedText = capturedText
                Log.d(TAG, "New unique text found. Processing: '$capturedText'")
                checkClipboardContent(capturedText)
            }
        }
    }

    private fun findTextInNode(nodeInfo: AccessibilityNodeInfo?): String? {
        if (nodeInfo == null) return null
        val builder = StringBuilder()
        if (nodeInfo.text != null && nodeInfo.text.isNotEmpty()) {
            builder.append(nodeInfo.text)
        }
        for (i in 0 until nodeInfo.childCount) {
            val child = nodeInfo.getChild(i)
            val childText = findTextInNode(child)
            if (!childText.isNullOrBlank()) {
                if (builder.isNotEmpty()) builder.append(" ")
                builder.append(childText)
            }
            child?.recycle()
        }
        return builder.toString().trim().takeIf { it.isNotEmpty() }
    }
    override fun onInterrupt() {
        Log.d(TAG, "Accessibility service interrupted.")
    }

    override fun onDestroy() {
        super.onDestroy()
        if (Build.VERSION.SDK_INT < 35) {
            Log.d(TAG, "Removing legacy clipboard listener.")
            try {
                clipboardManager.removePrimaryClipChangedListener(clipboardListener)
            } catch (e: Exception) {
                // Catching potential exceptions is good practice here
                Log.e(TAG, "Error removing clipboard listener", e)
            }
        }
        Log.d(TAG, "Accessibility Service destroyed.")
    }
}