package com.resdev.audioplayer.service

import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.util.Log
import com.resdev.audioplayer.model.items.SongItem
import com.resdev.audioplayer.player.PlayerController
import com.resdev.audioplayer.player.PlayerControllerGranter
import com.resdev.audioplayer.repositoty.Repository
import com.resdev.audioplayer.broadcast.StopOnHeadphoneExtractionBroadcast


class BackgroundSongPlayerService : Service(){
    var mediaPlayer: PlayerController =
        PlayerControllerGranter.getController()
    var notificationBuilder = MusicNotificationBuilder()
    init {
        mediaPlayer.bindService(this)
    }
    val notificationId = 1

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        Log.d("ServiceCreate", "onCreate: ")
        val receiver = StopOnHeadphoneExtractionBroadcast()
        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_HEADSET_PLUG)
        }
        registerReceiver(receiver, filter)
    }

    private fun handleIntent(intent: Intent?) {
        when(intent?.action){
            "start"->start(intent.getStringExtra("SongUri"))
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

    private fun setupNotification(songItem: SongItem){
        startForeground(notificationId, notificationBuilder.createNotification(songItem, applicationContext.packageName))
    }

    private fun start(songItem: String){
        Repository.getSongByUri(songItem)?.let {
            mediaPlayer.start(it)
        }

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
        mediaPlayer.pause()
        stopForeground(true)
    }

    fun songChanged(songItem: SongItem){
        setupNotification(songItem)
    }

    fun setNotificationPaused(){
        startForeground(notificationId, notificationBuilder.setPaused())
    }

    fun setNotificationResumed(){
        startForeground(notificationId, notificationBuilder.setResumed())
    }
}