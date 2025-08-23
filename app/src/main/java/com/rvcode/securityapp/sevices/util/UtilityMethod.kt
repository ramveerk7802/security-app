package com.rvcode.securityapp.sevices.util

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.rvcode.securityapp.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class UtilityMethod @Inject constructor(
    @ApplicationContext private val context: Context,
    private val notificationManager: NotificationManager
) {



    fun showAlertNotification(notificationId: Int,title: String, message: String){
        val channelId = "SECURITY_CHANNEL"
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            val channel = NotificationChannel(
                channelId,
                "Security Alerts",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        val alertNotification = NotificationCompat
            .Builder(context,channelId)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.warning)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setOngoing(false)
            .setAutoCancel(true)
            .build()
        notificationManager.notify(notificationId,alertNotification)

    }

    fun cancelNotification(notificationId: Int){
        notificationManager.cancel(notificationId)
    }

    fun notificationForServiceRunningOrActive(channelId: String,channelName: String, title: String, message: String): Notification{
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            val channel = NotificationChannel(
                channelId,
                "Security Alerts",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)

        }

        val notification = NotificationCompat.Builder(context,channelId)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.warning)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
        return notification;
    }
}