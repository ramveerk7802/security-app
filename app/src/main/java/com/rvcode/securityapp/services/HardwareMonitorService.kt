package com.rvcode.securityapp.services

import android.Manifest
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager

import android.content.pm.ServiceInfo
import android.hardware.camera2.CameraManager
import android.location.GnssStatus
import android.location.LocationManager
import android.media.AudioManager
import android.media.AudioRecordingConfiguration
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.content.ContextCompat
import com.rvcode.securityapp.services.util.UtilityMethod
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HardwareMonitorService : Service() {
    @Inject
    lateinit var audioManager: AudioManager
    @Inject
    lateinit var cameraManager: CameraManager

    @Inject
    lateinit var locationManager: LocationManager

    @Inject
    lateinit var utilityMethod: UtilityMethod

    private val TAG = "MYTAG"

    companion object {
        private const val FOREGROUND_ID = 1
        private const val CAMERA_NOTIFICATION_ID=101
        private const val MIC_NOTIFICATION_ID = 102
        private const val LOCATION_NOTIFICATION_ID = 103
    }


    // helper function for triger voice alert

    fun startVoiceAlert(message: String){
        val intent = Intent(this, VoiceAlertService::class.java)
        intent.putExtra("VOICE_MESSAGE",message)
        startService(intent)
    }





    private val cameraCallback = object : CameraManager.AvailabilityCallback(){
        override fun onCameraUnavailable(cameraId: String) {
            super.onCameraUnavailable(cameraId)
            val msg = "Privacy Alert! Camera is currently in use."
            utilityMethod.showAlertNotification(
                notificationId = CAMERA_NOTIFICATION_ID,
                title = "Privacy Alert!",
                message = "A camera is currently in use."
            )
            startVoiceAlert(msg)
        }

        override fun onCameraAvailable(cameraId: String) {
            super.onCameraAvailable(cameraId)
            utilityMethod.cancelNotification(notificationId = CAMERA_NOTIFICATION_ID)
        }
    }

    private val audioCallback  = if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.Q){
        object : AudioManager.AudioRecordingCallback(){
            override fun onRecordingConfigChanged(configs: List<AudioRecordingConfiguration?>?) {
                super.onRecordingConfigChanged(configs)
                configs?.let {
                    if(it.isNotEmpty()){
                        val msg = "Privacy Alert! Currently Microphone in use"
                        utilityMethod.showAlertNotification(
                            notificationId = MIC_NOTIFICATION_ID,
                            title = "Privacy Alert!",
                            message = "Currently Microphone in use"
                        )
                        startVoiceAlert(message = msg)
                    }else{
                        utilityMethod.cancelNotification(notificationId = MIC_NOTIFICATION_ID)
                    }
                }
            }
        }
    }else
        null

    private val gnssStatusCallback = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        object : GnssStatus.Callback() {
            override fun onStarted() {
                super.onStarted()
                val msg = "Privacy Alert! Location services are active."
                utilityMethod.showAlertNotification(
                    notificationId = LOCATION_NOTIFICATION_ID,
                    title = "Privacy Alert!",
                    message = "Location services are active."
                )
                startVoiceAlert(msg)
            }
            override fun onStopped() {
                super.onStopped()
                Log.d("MYTAG", "Location (GPS) is available.")
                utilityMethod.cancelNotification(notificationId = LOCATION_NOTIFICATION_ID)
            }
        }
    } else null


    override fun onCreate() {
        Log.d(TAG,"Hardware monitoring service created")
        super.onCreate()
        registerCallback()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val notification = utilityMethod.notificationForServiceRunningOrActive(
            channelId = "MONITOR_SERVICE_CHANNEL",
            channelName = "Monitoring Service",
            title = "Security Active",
            message = "Monitoring hardware resources"
        )
        Log.d(TAG,"Hardware monitoring service started")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(
                FOREGROUND_ID,
                notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_CAMERA or
                        ServiceInfo.FOREGROUND_SERVICE_TYPE_MICROPHONE or
                        ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION
            )
        } else {
            startForeground(FOREGROUND_ID, notification)
        }
        return START_STICKY
    }


    override fun onDestroy() {
        super.onDestroy()
        cameraManager.unregisterAvailabilityCallback(cameraCallback)
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.Q){
            audioCallback?.let { audioManager.unregisterAudioRecordingCallback(it) }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && gnssStatusCallback != null) {
            locationManager.unregisterGnssStatusCallback(gnssStatusCallback)
        }
    }

    private fun registerCallback() {
        cameraManager.registerAvailabilityCallback(cameraCallback, null)
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.Q){
            audioCallback?.let { audioManager.registerAudioRecordingCallback(it,null) }
        }

        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.N && gnssStatusCallback != null){
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                locationManager.registerGnssStatusCallback(gnssStatusCallback,null)
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null
}