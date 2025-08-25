package com.rvcode.securityapp.services

import android.app.Service
import android.content.Intent
import android.nfc.Tag
import android.os.IBinder
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.Locale

class VoiceAlertService : Service(), TextToSpeech.OnInitListener {

    private var tts: TextToSpeech?=null
    private var lastMessage: String?=null
    private val TAG = "VOICEALERTSERVICE"

    override fun onCreate() {
        super.onCreate()
        tts = TextToSpeech(this,this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        return super.onStartCommand(intent, flags, startId)
        val message = intent?.getStringExtra("VOICE_MESSAGE")
        if (message!=null){
            lastMessage=message
            speak(message)
        }
        return START_STICKY
    }


    override fun onInit(status: Int) {
        if(status== TextToSpeech.SUCCESS){
            tts?.language= Locale.US
            lastMessage?.let {
                speak(it)
            }
        }
    }


    private fun speak(message: String){
        if (tts!=null){
            tts?.speak(message, TextToSpeech.QUEUE_FLUSH,null,"")
//            Log.d(TAG,"Speak method called")
        }
    }

    override fun onDestroy() {

        tts?.stop()
        tts?.shutdown()
        super.onDestroy()
//        Log.d(TAG, "Stopped after speaking")
    }


    override fun onBind(intent: Intent?): IBinder? =null
}