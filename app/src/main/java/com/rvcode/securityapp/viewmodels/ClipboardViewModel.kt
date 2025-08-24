package com.rvcode.securityapp.viewmodels

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import com.rvcode.securityapp.sevices.ClipboardAccessibilityService

import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class ClipboardViewModel @Inject constructor(
    @ApplicationContext private val context: Context
): ViewModel() {

    fun startClipboardMonitoringService(){
        val intent = Intent(context, ClipboardAccessibilityService::class.java)
        context.startForegroundService(intent)
    }

    fun stopClipboardMonitoringService(){
        val intent = Intent(context, ClipboardAccessibilityService::class.java)
        context.stopService(intent)

    }
}