package com.rvcode.securityapp.viewmodels

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import com.rvcode.securityapp.sevices.HardwareMonitorService
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class MonitoringViewModel @Inject constructor(@ApplicationContext private val context: Context) : ViewModel() {

    fun startMonitoring() {
        val intent = Intent(context, HardwareMonitorService::class.java)
        context.startForegroundService(intent)
    }

    fun stopMonitoring() {
        val intent = Intent(context, HardwareMonitorService::class.java)
        context.stopService(intent)
    }
}