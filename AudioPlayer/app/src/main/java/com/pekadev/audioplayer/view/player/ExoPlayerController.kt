package com.pekadev.audioplayer.view.player


import android.media.MediaMetadataRetriever
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.pekadev.audioplayer.R
import com.pekadev.audioplayer.model.SongItem
import com.pekadev.audioplayer.repositoty.Repository
import com.pekadev.audioplayer.view.application.MyApplication
import com.pekadev.audioplayer.view.fragment.AudioSwitcherFragment
import com.pekadev.audioplayer.view.service.BackgroundSongPlayerService

object ExoPlayerController : PlayerController{
    //Controllers
    lateinit var  listAdapter: RecyclerView
    lateinit var service: BackgroundSongPlayerService
    lateinit var controllerFragment: AudioSwitcherFragment

    //Data
    var context = MyApplication.getApplicationContext()
    var player: SimpleExoPlayer
    val soundList = Repository.getData()

    //Player variables
    private var song = MutableLiveData<SongItem?>()
    private var lastSong: SongItem? = null
    private var lastPausedSong: SongItem? = null

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
    }

    override fun bindService(service: BackgroundSongPlayerService) {
        this.service = service
    }

    override fun start(songItem: SongItem) {
        setSong(songItem)
        val mediaSource = buildMediaSource(songItem.getUri())
        player.prepare(mediaSource)
        player.playWhenReady = true
        service.songChanged(songItem)
    }

    override fun pause() {
        player?.playWhenReady = false
        service.setNotificationPaused()
        lastPausedSong = getSong()
        setSong(null)
    }

    override fun stop() {
        player?.playWhenReady = false
    }

    override fun resume() {
        player.playWhenReady = true
        setSong(lastPausedSong)
        service.setNotificationResumed()
        lastPausedSong = null
    }

    override fun next(){
        setSong(Repository.getNextSong(getSong()!!))
        stop()
        start(
            getSong()!!
        )
    }
    override fun previous(){
        setSong(Repository.getPreviousSong(getSong()!!))
        stop()
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



     fun getSong(): SongItem?{
        return song.value
    }

    private fun setSong(songItem: SongItem?){
        lastSong = getSong()
        song.value = songItem
    }

    fun getObservableSongId():MutableLiveData<SongItem?>{
        return song
    }

    fun getLastSong() = lastSong
}