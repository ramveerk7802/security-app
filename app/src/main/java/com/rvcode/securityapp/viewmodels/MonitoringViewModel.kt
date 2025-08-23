package com.rvcode.securityapp.viewmodels

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.lifecycle.ViewModel

import com.rvcode.securityapp.sevices.HardwareMonitorService
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class MonitoringViewModel @Inject constructor(@ApplicationContext private val context: Context) : ViewModel() {

    fun startMonitoring() {
        val serviceIntent = Intent(context, HardwareMonitorService::class.java)
        context.startForegroundService(serviceIntent)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            context.startForegroundService(serviceIntent)
//        } else {
//            context.stopService(serviceIntent)
//        }
    }

    fun stopMonitoring() {
        val intent = Intent(context, HardwareMonitorService::class.java)
        context.stopService(intent)
    }
}