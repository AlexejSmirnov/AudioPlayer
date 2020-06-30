package com.pekadev.audioplayer.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.pekadev.audioplayer.model.SongItem
import com.pekadev.audioplayer.repositoty.Repository

class AudioListFragmentViewModel(application: Application) : AndroidViewModel(application){
    private val _musicData = Repository.getData()
    private val musicData: LiveData<ArrayList<SongItem>> = _musicData

    fun getMusicData() = musicData

    fun updateData(){
        Repository.refillDatabase()
    }
}