package com.rvcode.securityapp.di

import android.app.AppOpsManager
import android.app.NotificationManager
import android.content.ClipboardManager
import android.content.Context
import android.hardware.camera2.CameraManager
import android.location.LocationManager
import android.media.AudioManager
import androidx.core.content.ContextCompat
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesNotificationManager(@ApplicationContext context: Context): NotificationManager{
        return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    @Provides
    @Singleton
    fun provideClipboardManager(@ApplicationContext context: Context): ClipboardManager {
        return context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    }

    @Provides
    @Singleton
    fun provideLocationManager(@ApplicationContext context: Context): LocationManager {
        return context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    @Provides
    @Singleton
    fun provideCameraManager(@ApplicationContext context: Context): CameraManager {
        return context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    }
    @Provides
    @Singleton
    fun provideAudioManager(@ApplicationContext context: Context): AudioManager {
        return context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    }

}