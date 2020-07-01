package com.pekadev.audioplayer.view.player

import androidx.lifecycle.MutableLiveData
import com.pekadev.audioplayer.model.SongItem
import com.pekadev.audioplayer.view.service.BackgroundSongPlayerService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

interface PlayerController {
    fun bindService(service: BackgroundSongPlayerService)
    fun start(songItem: SongItem)
    fun pause()
    fun stop()
    fun resume()
    fun next()
    fun previous()
    fun isPlaying(): Boolean
    fun getSong(): SongItem?
    fun getObservableSongId(): MutableLiveData<SongItem?>
    fun getLastSong():SongItem?
    fun changePositions(songItem: SongItem?)
    fun setPosition(percents: Float)
    fun getPosition(): MutableLiveData<Float>
}