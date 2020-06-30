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
    lateinit var  listAdapter: RecyclerView
    lateinit var service: BackgroundSongPlayerService
    lateinit var controllerFragment: AudioSwitcherFragment
    private lateinit var songItem: SongItem

    var context = MyApplication.getApplicationContext()
    var songIndex = MutableLiveData<Int>()
    var player: SimpleExoPlayer
    val soundList = Repository.getData()
    private var lastPausedInt = -1
    private var lastSong = -1
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

    override fun start(index: Int?) {
        if (index!=null){
            setSongId(index)
        }
        if (getSongId() ==-1){return}
        val mediaSource = buildMediaSource(soundList.value!![getSongId()].getUri())
        player.prepare(mediaSource)
        player.playWhenReady = true
        service.songChanged(soundList.value!![getSongId()])
    }

    override fun pause() {
        player?.playWhenReady = false
        service.setNotificationPaused()
        lastPausedInt = getSongId()
        setSongId(-1)
    }

    override fun stop() {
        player?.playWhenReady = false
    }

    override fun resume() {
        player.playWhenReady = true
        setSongId(lastPausedInt)
        service.setNotificationResumed()
        lastPausedInt = -1
    }

    override fun next(){
        setSongId(getSongId()+1)
        if (getSongId() == soundList.value?.size){
            setSongId(0)
        }
        stop()
        start(
            getSongId()
        )
    }
    override fun previous(){
        setSongId(getSongId()-1)
        if (getSongId() <0){
            setSongId(soundList.value?.lastIndex ?: 0)
        }
        stop()
        start(
            getSongId()
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
    fun getCurrentPlayingSong():Int{
        if(player.isPlaying){
            return songIndex.value!!
        }
        return -1
    }

    private fun getSongId(): Int{
        return songIndex.value ?: -1
    }

    private fun setSongId(value: Int){
        lastSong = getSongId()
        if (value>=0){
            setMetadata(value)
        }
        songIndex.value = value
    }

    fun getObservableSongId():MutableLiveData<Int>{
        return songIndex
    }
    private fun setMetadata(id: Int){
        songItem = soundList.value!![id]
    }
    fun getMetadata() = songItem
    fun getLastSong() = lastSong
}