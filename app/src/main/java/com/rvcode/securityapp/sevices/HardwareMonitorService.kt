package com.rvcode.securityapp.sevices

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.hardware.camera2.CameraManager
import android.location.LocationManager
import android.media.AudioManager
import android.media.AudioRecordingConfiguration
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HardwareMonitorService @Inject constructor(
    private val notificationManager: NotificationManager,
    private val cameraManager: CameraManager,
    private val audioManager: AudioManager,
    private val locationManager: LocationManager
): Service() {


    // for camera availability check
    private val cameraCallback = object : CameraManager.AvailabilityCallback(){
        override fun onCameraAvailable(cameraId: String) {
            super.onCameraAvailable(cameraId)
        }

        override fun onCameraUnavailable(cameraId: String) {
            super.onCameraUnavailable(cameraId)
            showNotification(title = "Camera in use", message = "An app is currently using the camera.")
        }

    }

    private val audioCallback = if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.Q) object : AudioManager.AudioRecordingCallback(){
        override fun onRecordingConfigChanged(configs: MutableList<AudioRecordingConfiguration>?) {
            super.onRecordingConfigChanged(configs)
            if(configs?.isNotEmpty() ?: false){
                showNotification(title = "Audio in use", message = "An app is currently using audio.")
            }
        }
    }
    else null


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        return super.onStartCommand(intent, flags, startId)
        startForeground(1,createNotification())

        //Register camera monitoring
        cameraManager.registerAvailabilityCallback(cameraCallback,null)

        //Register mic monitoring
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.Q){
            audioManager.registerAudioRecordingCallback(audioCallback!!,null)
        }

        // Location check
        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER)){
            showNotification(title = "Location in use", message = "An app is currently using location.")
        }

        return START_STICKY

    }


    private fun showNotification(title:String, message:String){

        val channelId = "HARDWARE_ALERTS"
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            val channel  = NotificationChannel(channelId,"Hardware Alerts", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        val alertNotification = NotificationCompat.Builder(this,channelId)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(),alertNotification)

    }

    private fun createNotification() : Notification{
        val channelId = "MONITOR_SERVICE_CHANNEL"
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            val channel  = NotificationChannel(channelId,"Monitoring Service", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }
        return NotificationCompat.Builder(this,channelId)
            .setContentTitle("Security Service Running")
            .setContentText("Monitor hardware resources")
            .build()
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraManager.unregisterAvailabilityCallback(cameraCallback)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}