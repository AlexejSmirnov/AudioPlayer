package com.pekadev.audioplayer.view.application

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

class MyApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        CONTEXT = applicationContext
        createNotificationChannel()
    }
    companion object{
        private lateinit var CONTEXT: Context
        fun getApplicationContext() =
            CONTEXT
        const val CHANNEL_ID = "SoundNotificationChannel"
    }

    fun createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            var soundChannel = NotificationChannel(
                CHANNEL_ID,
                "MyPlayer Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            soundChannel.setSound(null, null);
            soundChannel.enableVibration(false)
            var notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(soundChannel)
        }
    }
}