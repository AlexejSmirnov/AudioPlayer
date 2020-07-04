package com.pekadev.audioplayer.player


import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.pekadev.audioplayer.R
import com.pekadev.audioplayer.Util.millisToStringTime
import com.pekadev.audioplayer.model.SongItem
import com.pekadev.audioplayer.repositoty.Repository
import com.pekadev.audioplayer.view.application.MyApplication
import com.pekadev.audioplayer.view.service.BackgroundSongPlayerService
import kotlinx.coroutines.*

class ExoPlayerController : PlayerController{
    //Controllers
    lateinit var service: BackgroundSongPlayerService

    //Data
    var context = MyApplication.getApplicationContext()
    var player: SimpleExoPlayer
    //Player variables
    private var song = MutableLiveData<SongItem?>()
    private var lastSong: SongItem? = null
    private var lastPausedSong: SongItem? = null
    private var position = MutableLiveData<Float>()

    init {
        val audioAttributes =
            AudioAttributes.Builder()
                .setUsage(C.USAGE_MEDIA)
                .setContentType(C.CONTENT_TYPE_MUSIC)
                .build()
        player = ExoPlayerFactory.newSimpleInstance(
                context,
            DefaultRenderersFactory(context),
            DefaultTrackSelector(),
            DefaultLoadControl()
        )
        player.audioAttributes = audioAttributes
        player.addListener(object :Player.EventListener{
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                if (playbackState == ExoPlayer.STATE_ENDED){
                    next()
                }
            }

        })
    }

    override fun bindService(service: BackgroundSongPlayerService) {
        this.service = service
    }


    override fun start(songItem: SongItem) {
        val mediaSource = buildMediaSource(songItem.getUri())
        player.prepare(mediaSource)
        player.playWhenReady = true
        setSong(songItem)
        service.songChanged(songItem)
        changePositions(getSong())
    }

    override fun pause() {
        player?.playWhenReady = false
        service.setNotificationPaused()
        lastPausedSong = getSong()
        setSong(null)
    }
    override fun resume() {
        player.playWhenReady = true
        setSong(lastPausedSong)
        service.setNotificationResumed()
        lastPausedSong = null
        changePositions(getSong())
    }

    override fun next(){
        setSong(Repository.getNextSong(getSong()?:lastPausedSong?:lastSong!!))
        start(
            getSong()!!
        )
    }
    override fun previous(){
        setSong(Repository.getPreviousSong(getSong()?:lastPausedSong?:lastSong!!))
        start(
            getSong()!!
        )
    }

    override fun isPlaying(): Boolean {
        return player.isPlaying
    }

    private fun buildMediaSource(uri: Uri): MediaSource {
        return ProgressiveMediaSource.Factory(
            DefaultDataSourceFactory(
                context,
                Util.getUserAgent(context, context.getString(R.string.app_name))
            )
        ).createMediaSource(uri)
    }

     override fun getSong(): SongItem?{
        return song.value
    }

    private fun setSong(songItem: SongItem?){
        lastSong = getSong()
        song.value = songItem
    }

    override fun getObservableSong():MutableLiveData<SongItem?>{
        return song
    }

    override fun getLastSong() = lastSong

    override fun changePositions(songItem: SongItem?){
        GlobalScope.launch {
            while (song.value==songItem) {
                withContext(Dispatchers.Main){
                    position.value =((player.currentPosition.toFloat()/player.duration))
                }
                delay(500)
            }
        }
    }

    override fun setPosition(percents: Float){
        if(percents>1 || percents<0){
            return
        }
        player.seekTo((player.duration*percents).toLong())
    }

    override fun getPosition() = position

    override fun getPausedSong(): SongItem? {
        return lastPausedSong
    }

    override fun getLength(): Long {
        return  if (player.duration>0){
            player.duration
        }
        else{
            0
        }
    }

}