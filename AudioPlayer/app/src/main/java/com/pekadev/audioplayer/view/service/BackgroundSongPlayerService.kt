package com.pekadev.audioplayer.view.service

import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import com.pekadev.audioplayer.model.SongItem
import com.pekadev.audioplayer.repositoty.Repository
import com.pekadev.audioplayer.player.PlayerController
import com.pekadev.audioplayer.player.PlayerControllerGranter
import com.pekadev.audioplayer.view.broadcast.StopOnHeadphoneExtractionBroadcast


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
        val receiver = StopOnHeadphoneExtractionBroadcast()
        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_HEADSET_PLUG)
        }
        registerReceiver(receiver, filter)
    }

    private fun handleIntent(intent: Intent?) {
        when(intent?.action){
            "start"->start(intent!!.getStringExtra("SongUri"))
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
        mediaPlayer.start(Repository.getSongByUri(songItem)!!)
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