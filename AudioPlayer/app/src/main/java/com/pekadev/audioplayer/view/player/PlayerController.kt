package com.pekadev.audioplayer.view.player

import com.pekadev.audioplayer.model.SongItem
import com.pekadev.audioplayer.view.service.BackgroundSongPlayerService

interface PlayerController {
    fun bindService(service: BackgroundSongPlayerService)
    fun start(songItem: SongItem)
    fun pause()
    fun stop()
    fun resume()
    fun next()
    fun previous()
    fun isPlaying(): Boolean
}