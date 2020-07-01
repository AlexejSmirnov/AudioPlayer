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
    private val sortedMusicPaths = musicPaths
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
                if ((i+1)%50==0){
                    musicPaths.postValue(list)
                }
            }
            musicPaths.postValue(list)

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
    fun getData() = sortedMusicPaths

    fun getNextSong(songItem: SongItem): SongItem?{
        var sortedList = sortedMusicPaths.value!!
        var index: Int = sortedList.indexOf(songItem)
        if (index != -1){
            return if (index==sortedList.lastIndex){
                sortedList[0]
            } else{
                sortedList[index+1]
            }
        }
        else{
            var fullList = musicPaths.value!!
            index = fullList.indexOf(songItem)
            return if (index==fullList.lastIndex){
                fullList[0]
            } else{
                fullList[index+1]
            }
        }
    }

    fun getPreviousSong(songItem: SongItem): SongItem?{
        var sortedList = sortedMusicPaths.value!!
        var index: Int = sortedList.indexOf(songItem)
        if (index != -1){
            return if (index==0){
                sortedList[sortedList.lastIndex]
            } else{
                sortedList[index-1]
            }
        }
        else{
            var fullList = musicPaths.value!!
            index = fullList.indexOf(songItem)
            return if (index==0){
                fullList[sortedList.lastIndex]
            } else{
                fullList[index-1]
            }
        }
    }

    fun getSongByUri(uri: String):SongItem?{
        val realUri = Uri.parse(uri)
        for (i in musicPaths.value!!){
            if (i.getUri()==realUri)
                return i
        }
        return null
    }
}