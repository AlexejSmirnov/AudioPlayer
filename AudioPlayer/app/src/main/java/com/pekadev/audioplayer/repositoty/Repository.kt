package com.pekadev.audioplayer.repositoty

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.pekadev.audioplayer.model.FolderScanner
import com.pekadev.audioplayer.model.SongItem
import com.pekadev.audioplayer.model.room.UriDatabase
import com.pekadev.audioplayer.model.room.UriEntity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

object Repository {
    private val musicPaths = MutableLiveData<ArrayList<SongItem>>()
    private val database = UriDatabase.getDatabase()
    init {
        loadData()

    }
    fun loadData() {
        GlobalScope.launch {
            val data = database.uriDao().selectAllUri()
            val list = ArrayList<SongItem>()
            for (i in data.indices){
                val song = SongItem.createSongItem(Uri.parse(data[i].uri))
                if (song!=null){
                    list.add(song)
                }
                if (i%50==0){
                    musicPaths.postValue(list)
                }
            }

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