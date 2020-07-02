package com.pekadev.audioplayer.view.player

import androidx.lifecycle.MutableLiveData
import com.pekadev.audioplayer.model.SongItem
import com.pekadev.audioplayer.view.service.BackgroundSongPlayerService

interface PlayerController {
    fun bindService(service: BackgroundSongPlayerService)
    fun start(songItem: SongItem)
    fun pause()
    fun resume()
    fun next()
    fun previous()
    fun isPlaying(): Boolean //does the player play
    fun getSong(): SongItem? //current playing song
    fun getPausedSong(): SongItem? //current paused song
    fun getObservableSong(): MutableLiveData<SongItem?> //get observable song, that player currently plays
    fun getLastSong():SongItem? //get song, that was played before current
    fun changePositions(songItem: SongItem?) //start to change observable position of song
    fun setPosition(percents: Float) //set position of song (from 0 to 1)
    fun getPosition(): MutableLiveData<Float> //get observable position of song
    fun getLength():Long //song length
}