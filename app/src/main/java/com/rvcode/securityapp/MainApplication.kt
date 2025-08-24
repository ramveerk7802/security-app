package com.rvcode.securityapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SecurityApp : Application() {
//    override fun onCreate() {
//        super.onCreate()
//        // Start the service as soon as the app process begins
//        startForegroundService(Intent(this, HardwareMonitorService::class.java))
//    }
}