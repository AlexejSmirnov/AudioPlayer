package com.resdev.audioplayer.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.resdev.audioplayer.model.items.SongItem
import com.resdev.audioplayer.repositoty.Repository

class AudioListFragmentViewModel(application: Application) : AndroidViewModel(application){
    private val _musicData = Repository.getData()

    fun getMusicData():LiveData<ArrayList<SongItem>> = _musicData


}