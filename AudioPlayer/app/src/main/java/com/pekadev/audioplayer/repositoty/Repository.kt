package com.pekadev.audioplayer.repositoty

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.pekadev.audioplayer.model.FolderScanner
import com.pekadev.audioplayer.model.room.UriDatabase
import com.pekadev.audioplayer.model.room.UriEntity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

object Repository {
    private val musicPaths = MutableLiveData<ArrayList<Uri>>()
    private val database = UriDatabase.getDatabase()
    init {
        loadData()

    }
    fun loadData() {
        GlobalScope.launch {
            val data = database.uriDao().selectAllUri()
            musicPaths.postValue( ArrayList((data.map { Uri.parse(it.uri) })))

        }

    }

    fun refillDatabase(){
        GlobalScope.launch {
            val values = FolderScanner.getMediaFileList()
            for (i in values){
                database.uriDao().insertUri(UriEntity(i.toString()))
            }
            loadData()
        }

    }
    fun getData() = musicPaths

}