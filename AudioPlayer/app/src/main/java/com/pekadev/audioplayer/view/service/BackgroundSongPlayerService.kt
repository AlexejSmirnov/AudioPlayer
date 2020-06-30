package com.pekadev.audioplayer.view.service

import android.app.Service
import android.content.Intent
import android.net.Uri
import android.os.IBinder
import com.pekadev.audioplayer.view.player.PlayerController
import com.pekadev.audioplayer.view.player.ExoPlayerController


class BackgroundSongPlayerService : Service(){
    var mediaPlayer: PlayerController =
        ExoPlayerController
    var notificationBuilder = MusicNotificationBuilder()
    init {
        mediaPlayer.bindService(this)

    }
    val notificationId = 1

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun handleIntent(intent: Intent?) {
        when(intent?.action){
            "start"->start(intent!!.getIntExtra("SongUri", -1))
            "pause" -> pauseOrResume()
            "next"->  mediaPlayer.next()
            "previous"->mediaPlayer.previous()
            "close"-> stopForeground()
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        handleIntent(intent)
        return START_NOT_STICKY
    }

    private fun setupNotification(uri: Uri){
        startForeground(notificationId, notificationBuilder.createNotification(uri, applicationContext.packageName))
    }

    private fun start(index: Int?){
        mediaPlayer.start(index)
    }

    private fun pauseOrResume(){
        if(mediaPlayer.isPlaying()){
            mediaPlayer.pause()

        }
        else{
            mediaPlayer.resume()

        }
    }


    private fun stopForeground() {
        mediaPlayer.stop()
        stopForeground(true)
    }

    fun songChanged(uri: Uri){
        setupNotification(uri)
    }

    fun setNotificationPaused(){
        startForeground(notificationId, notificationBuilder.setPaused())
    }

    fun setNotificationResumed(){
        startForeground(notificationId, notificationBuilder.setResumed())
    }
}